<template>
  <div class="container-fluid chat-app">
    <div class="row">
      <!-- Chat Sidebar -->
      <div v-if="students.length > 0" class="col-md-4 col-lg-3 p-0 chat-sidebar">
        <div class="list-group list-group-flush">
          <a
              v-for="(student, index) in students"
              :key="student.id"
              href="#"
              class="list-group-item list-group-item-action"
              :class="{ active: selectedStudentIndex === index }"
              @click="selectStudent(index)"
          >
            <div class="student-icon me-2">
              {{ student.name.firstName.charAt(0).toUpperCase() }}
            </div>
            {{ student.name.firstName }}
            <span v-if="getUnreadCount(student) > 0" class="badge bg-primary ms-2">
                  {{ getUnreadCount(student) }}
            </span>
          </a>
        </div>
      </div>

      <!-- Chat Window -->
      <div class="col-md-8 col-lg-9 p-0 chat-window" v-if="selectedStudent">
        <div class="card h-100">
          <div class="card-header bg-light d-flex align-items-center">
            <div class="student-icon me-2">
              {{ selectedStudent?.name.firstName.charAt(0).toUpperCase() || '' }}
            </div>
            <i class="bi bi-person-circle me-2" style="font-size: 1.5rem;"></i>
            <h5 class="mb-0">{{ selectedStudent?.name.firstName + ' ' +  selectedStudent?.name.lastName || "Select a student" }}</h5>
          </div>
          <div class="card-body chat-body">
            <div class="messages-container">
              <div
                  v-for="(message, index) in selectedStudent?.messages || []"
                  :key="index"
                  class="d-flex mb-2"
                  :class="message.senderId === landlord.userId ? 'justify-content-end' : 'justify-content-start'"
              >
                <div v-if="message.senderId !== landlord.userId" class="student-icon">
                  {{ students.find(s => s.id === message.senderId)?.name.firstName.charAt(0).toUpperCase() }}
                </div>
                <div
                    :class="message.senderId === landlord.userId ? 'chat-bubble chat-bubble-right' : 'chat-bubble chat-bubble-left'"
                >
                  {{ message.text }}
                  <div class="message-time">{{ formatTimestamp(message.timestamp) }}</div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-footer bg-light">
            <div class="input-group">
              <input
                  v-model="newMessage"
                  type="text"
                  class="form-control"
                  placeholder="Type your message..."
                  :disabled="!selectedStudent || isSending"
                  @keydown.enter="sendMessage"
              />
              <button class="btn btn-primary" type="button" @click="sendMessage" :disabled="!selectedStudent || isSending">
                <span v-if="isSending">Sending...</span>
                <span v-else>Send</span>
              </button>
            </div>
            <div v-if="errorMessage" class="alert alert-danger mt-2">{{ errorMessage }}</div>
          </div>
        </div>
      </div>

      <!-- Prompt for no messages -->
      <div v-else class="col-md-12 p-0 d-flex align-items-center justify-content-center">
        <div class="text-muted">No messages yet!</div>
      </div>
    </div>
  </div>
</template>

<script>
//import io from 'socket.io-client';
import LandlordService from '@/service/LandlordService';
import MessageService from '@/service/MessageService';

export default {
  name: 'LandlordChat',
  data() {
    return {
      landlord: null,
      students: [],
      selectedStudentIndex: 0,
      newMessage: '',
      socket: null,
      errorMessage: '',
      isSending: false,
      lastViewed: {},
    };
  },
  computed: {
    selectedStudent(index) {
      this.selectedStudentIndex = index;

      // Update the last viewed timestamp for this student
      const studentId = this.selectedStudent.id;
      this.lastViewed[studentId] = new Date().toISOString();
    },
  },
  methods: {
    setupWebSocket() {
      // Assign the WebSocket instance to this.socket
      this.socket = new WebSocket('ws://localhost:8080/ws/messages');

      // Handle incoming messages
      this.socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        this.updateMessages(message);
      };

      // Handle connection open
      this.socket.onopen = () => {
        console.log('WebSocket connection established');
      };

      // Handle connection close
      this.socket.onclose = () => {
        console.warn('WebSocket connection closed. Attempting to reconnect...');
        this.socket = null;

        // Try to reconnect after a delay
        setTimeout(() => {
          this.setupWebSocket();
        }, 3000);
      };

      // Handle any errors
      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
    },

    getUnreadCount(student) {
      const lastViewedTime = this.lastViewed[student.id];
      if (!lastViewedTime) return student.messages.length; // All messages are unread if never viewed

      // Count messages with a timestamp greater than lastViewedTime
      return student.messages.filter(
          (message) => new Date(message.timestamp) > new Date(lastViewedTime)
      ).length;
    },

    updateMessages(message) {
      const student = this.students.find(
          s => s.id === message.senderId || s.id === message.receiverId
      );

      if (student) {
        student.messages.push(message);
      } else {
        this.fetchLandlordAndConversations();
      }

      // Optionally, if the current student is selected, mark messages as viewed
      if (this.selectedStudent && this.selectedStudent.id === message.senderId) {
        this.lastViewed[message.senderId] = new Date().toISOString();
      }
    },

    pollMessages() {
      setInterval(async () => {
        await this.fetchLandlordAndConversations(this.selectedStudent?.id);
      }, 5000); // Poll every 5 seconds
    },
    async fetchLandlordAndConversations(selectedStudentId = null) {
      try {
        const landlordId = 4;
        this.landlord = await LandlordService.readLandlord(landlordId);
        const messages = await MessageService.fetchMessagesForUser(landlordId);

        if (messages.length === 0) {
          this.students = [];
          return;
        }

        const studentMap = {};
        messages.forEach((message) => {
          const otherUser = message.sender.userId === landlordId ? message.receiver : message.sender;

          if (!studentMap[otherUser.userId]) {
            studentMap[otherUser.userId] = {
              id: otherUser.userId,
              name: otherUser.name,
              messages: [],
            };
          }

          studentMap[otherUser.userId].messages.push({
            senderId: message.sender.userId,
            text: message.content,
            timestamp: message.timestamp, // Added timestamp
          });
        });

        this.students = Object.values(studentMap);

        // if (this.students.length > 0) {
        ///   this.selectedStudentIndex = 0;
        //}
        // Find the index of the student that was selected before
        const previousIndex = selectedStudentId
            ? this.students.findIndex(student => student.id === selectedStudentId)
            : -1;

        // If a previously selected student exists, retain that selection
        if (previousIndex !== -1) {
          this.selectedStudentIndex = previousIndex;
        } else if (this.students.length > 0) {
          this.selectedStudentIndex = 0; // Default to the first student if no previous selection exists
        }

      } catch (error) {
        this.errorMessage = 'Error fetching conversations. Please try again later.';
        console.error('Error fetching landlord or conversations:', error);
      }
    },
    selectStudent(index) {
      this.selectedStudentIndex = index;
    },
    async sendMessage() {
      if (this.newMessage.trim() !== '' && this.selectedStudent) {
        const messageDTO = {
          content: this.newMessage,
          senderId: this.landlord.userId,
          receiverId: this.selectedStudent.id,
          timestamp: new Date().toISOString(),
        };

        this.isSending = true;

        try {
          await MessageService.sendMessage(messageDTO);

          // Check if the WebSocket is open before attempting to send a message
          if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(messageDTO));
          } else {
            console.warn('WebSocket is not open. Unable to send message.');
            this.errorMessage = 'WebSocket connection is not available. Message may not be sent in real-time.';
          }

          // Add the message to the local list
          this.selectedStudent.messages.push({
            senderId: this.landlord.userId,
            text: this.newMessage,
            timestamp: messageDTO.timestamp,
          });

          // Clear any previous error messages
          this.errorMessage = '';
          this.newMessage = '';
        } catch (error) {
          this.errorMessage = 'Error sending message. Please try again later.';
          console.error('Error sending message:', error);
        } finally {
          this.isSending = false;
        }
      }
    },


    formatTimestamp(timestamp) {
      console.log("Timestamp:", timestamp); // Log the timestamp value
      const messageDate = new Date(timestamp);
      return messageDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
    }
  },
  async mounted() {
    await this.fetchLandlordAndConversations();
    this.setupWebSocket();
    this.pollMessages();

  },

};
</script>

<style scoped>
/* Same styles as before */
.chat-app {
  height: 100vh;
}

.chat-sidebar {
  background-color: #d8d8d8;
  border-right: 1px solid #ddd;
  overflow-y: auto;
}

.student-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  background-color: #127670; /* Icon background color */
  color: white;
  border-radius: 50%;
  font-weight: bold;
  text-align: center; /* Ensure text is centered */
}

.chat-window {
  display: flex;
  flex-direction: column;
  height: 100vh; /* Full height for the chat window */
}

.card-body {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 150px); /* Adjust based on header and footer height */
  padding: 0;
}

.messages-container {
  overflow-y: auto; /* Allow scrolling if messages exceed container height */
  flex-grow: 1; /* Take available space */
  padding: 20px;
  max-height: 100%; /* Ensure container fills available space */
}

.chat-bubble {
  position: relative; /* Allows absolute positioning of the timestamp */
  max-width: 75%;
  padding: 10px 15px;
  padding-bottom: 20px; /* Increase bottom padding to make space for the timestamp */
  border-radius: 20px;
  margin-bottom: 10px; /* Increase bottom margin to ensure spacing between messages */
}

.chat-bubble-left {
  background-color: #f1f1f1; /* Bubble color for received messages */
}

.chat-bubble-right {
  background-color: #127670; /* Bubble color for sent messages */
  color: white;
}

.message-time {
  position: absolute; /* Positioned relative to .chat-bubble */
  bottom: 5px; /* Distance from the bottom of the bubble */
  right: 10px; /* Distance from the right edge of the bubble */
  font-size: 0.6em;
  color: gray;
}

.alert {
  border-radius: 5px; /* Round the corners of the alert box */
  padding: 15px; /* Add some padding inside the alert box */
  margin-bottom: 20px; /* Add some space below the alert box */
  background-color: #f8d7da; /* Light red background for alerts */
  color: #721c24; /* Dark red text color for alerts */
  border: 1px solid #f5c6cb; /* Red border for the alert box */
}

</style>
