<template>
  <div class="container-fluid chat-app">
    <div class="row">
      <!-- Chat Sidebar for Listing Landlords -->
      <div v-if="landlords.length > 0" class="col-md-4 col-lg-3 p-0 chat-sidebar">
        <div class="list-group list-group-flush">
          <!-- List of landlords -->
          <a
              v-for="(landlord, index) in landlords"
              :key="landlord.id"
              href="#"
              class="list-group-item list-group-item-action d-flex align-items-center"
              :class="{ active: selectedLandlordIndex === index }"
              @click="selectLandlord(index)"
          >
            <div class="landlord-icon me-2">
              {{ landlord.name.firstName.charAt(0).toUpperCase() }}
            </div>
            <span>{{ landlord.name.firstName }}</span>

            <!-- Unread messages count badge aligned to the right -->
            <span v-if="getUnreadCount(landlord) > 0" class="badge bg-success ms-auto">
              {{ getUnreadCount(landlord) }}
           </span>
          </a>

        </div>
      </div>

      <!-- Chat Window -->
      <div class="col-md-8 col-lg-9 p-0 chat-window" v-if="selectedLandlord">
        <div class="card h-100">
          <div class="card-header bg-light d-flex align-items-center">
            <!-- Landlord's Name and Icon -->
            <div class="landlord-icon me-2">
              {{ selectedLandlord.name.firstName.charAt(0).toUpperCase() || '' }}
            </div>
            <i class="bi bi-person-circle me-2" style="font-size: 1.5rem;"></i>
            <h5 class="mb-0">{{ selectedLandlord.name.firstName || "Select a landlord" }}</h5>
          </div>
          <div class="card-body chat-body">
            <!-- Display chat messages -->
            <div class="messages-container">
              <div
                  v-for="(message, index) in selectedLandlord?.messages || []"
                  :key="index"
                  class="d-flex mb-2"
                  :class="message.senderId === student.userId ? 'justify-content-end' : 'justify-content-start'"
              >
                <!-- Only show the icon for the landlord -->
                <div v-if="message.senderId !== student.userId" class="landlord-icon">
                  {{ selectedLandlord.name.firstName.charAt(0).toUpperCase() }}
                </div>
                <div
                    :class="message.senderId === student.userId ? 'chat-bubble chat-bubble-right' : 'chat-bubble chat-bubble-left'"
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
                  :disabled="!selectedLandlord"
              />
              <button class="btn btn-primary" type="button" @click="sendMessage" :disabled="!selectedLandlord">
                Send
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- If no landlord is selected or no messages exist, show a prompt -->
      <div v-else class="col-md-12 p-0 d-flex align-items-center justify-content-center">
        <div class="text-muted">No messages yet!</div>
      </div>
    </div>
  </div>
</template>

<script>
import StudentService from '@/service/StudentService';
import MessageService from '@/service/MessageService';

export default {
  name: 'StudentChat',
  data() {
    return {
      student: null, // Stores student information
      landlords: [], // List of landlords who have messaged the student
      selectedLandlordIndex: 0, // Default to the first landlord
      newMessage: '',
      isSending: false,
      lastViewed: {},
    };
  },
  computed: {
    selectedLandlord() {
      return this.landlords[this.selectedLandlordIndex] || null;
    },
  },
  methods: {
    async fetchStudentAndConversations(selectedLandlordId = null) {
      try {
        const studentId = 1; // Use correct student ID
        this.student = await StudentService.readStudent(studentId);
        const messages = await MessageService.fetchMessagesForUser(studentId);

        if (messages.length === 0) {
          this.landlords = [];
          return;
        }

        const landlordMap = {};
        messages.forEach((message) => {
          const otherUser = message.sender.userId === studentId ? message.receiver : message.sender;

          if (!landlordMap[otherUser.userId]) {
            landlordMap[otherUser.userId] = {
              id: otherUser.userId,
              name: otherUser.name,
              messages: [],
            };
          }

          landlordMap[otherUser.userId].messages.push({
            senderId: message.sender.userId,
            text: message.content,
            timestamp: message.timestamp,

          });
          console.log('Message timestamp:', message.timestamp);
        });

        this.landlords = Object.values(landlordMap);

        // Retain selection or select the first landlord
        const previousIndex = selectedLandlordId
            ? this.landlords.findIndex(landlord => landlord.id === selectedLandlordId)
            : -1;

        if (previousIndex !== -1) {
          this.selectedLandlordIndex = previousIndex;
        } else if (this.landlords.length > 0) {
          this.selectedLandlordIndex = 0;
        }
      } catch (error) {
        console.error('Error fetching student or conversations:', error);
      }
    },


    updateMessages(message) {
      const landlord = this.landlords.find(
          l => l.id === message.senderId || l.id === message.receiverId
      );

      if (landlord) {
        landlord.messages.push(message);
      } else {
        this.fetchStudentAndConversations();
      }

      // Optionally, if the current landlord is selected, mark messages as viewed
      if (this.selectedLandlord && this.selectedLandlord.id === message.senderId) {
        this.lastViewed[message.senderId] = new Date().toISOString();

        // Save to localStorage so it's persisted across reloads
        localStorage.setItem('lastViewed', JSON.stringify(this.lastViewed));
      }
    },

    getUnreadCount(landlord) {
      const lastViewedTime = this.lastViewed[landlord.id];
      if (!lastViewedTime) return landlord.messages.length; // All messages are unread if never viewed

      // Count messages with a timestamp greater than lastViewedTime
      return landlord.messages.filter(
          (message) => new Date(message.timestamp) > new Date(lastViewedTime)
      ).length;
    },
    selectLandlord(index) {
      if (index >= 0 && index < this.landlords.length) {
        this.selectedLandlordIndex = index;

        // Update the last viewed timestamp for this student
        const landlordId = this.selectedLandlord.id;
        const currentTime = new Date().toISOString();

        // Update the last viewed timestamp for the selected student
        this.lastViewed[landlordId] = currentTime;

        // Save lastViewed state to localStorage
        localStorage.setItem('lastViewed', JSON.stringify(this.lastViewed));
      }
    },
    async sendMessage() {
      if (this.newMessage.trim() !== '' && this.selectedLandlord) {
        const messageDTO = {
          content: this.newMessage,
          senderId: this.student.userId, // Use the student's userId
          receiverId: this.selectedLandlord.id, // Send to the selected landlord's userId
          timestamp: new Date().toISOString(), // Include a timestamp for consistency
        };

        try {
          // Send the message to the backend service (e.g., save to the database)
          await MessageService.sendMessage(messageDTO);

          // Check if the WebSocket is open before attempting to send the message in real-time
          if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(messageDTO));
          } else {
            console.warn('WebSocket is not open. Unable to send message.');
            this.errorMessage = 'WebSocket connection is not available. Message may not be sent in real-time.';
          }

          // Add the message to the local state for immediate feedback in the chat
          this.selectedLandlord.messages.push({
            senderId: this.student.userId,
            text: this.newMessage,
            timestamp: messageDTO.timestamp, // Include the timestamp
          });

          // Clear the input field for new messages
          this.newMessage = '';
          this.errorMessage = ''; // Clear any existing error messages
        } catch (error) {
          console.error('Error sending message:', error);
          this.errorMessage = 'Error sending message. Please try again later.';
        }
      }
    },

    formatTimestamp(timestamp) {
      const messageDate = new Date(timestamp);
      return messageDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
    },

    pollMessages() {
      setInterval(async () => {
        await this.fetchStudentAndConversations();
      }, 5000); // Poll every 5 seconds
    },
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

  },
  async mounted() {
    // Fetch the student and conversations when the component is mounted
    await this.fetchStudentAndConversations();
    this.setupWebSocket();
    this.pollMessages();
  },
};
</script>

<style scoped>
.chat-app {
  height: 100vh; /* Full height for the chat app */
}

.chat-sidebar {
  background-color: #d8d8d8; /* Same as the form background */
  border-right: 1px solid #ddd;
  overflow-y: auto; /* Allow scrolling if the list of landlords is long */
}

.landlord-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  background-color: #127670;
  color: white;
  border-radius: 50%;
  font-weight: bold;
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
  overflow-y: auto; /* Enable scrolling if the content grows */
  flex-grow: 1;
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

</style>
