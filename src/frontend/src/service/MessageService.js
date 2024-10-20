//import axiosInstance from './axiosInstance';

import axiosInstance from 'axios';

class MessageService {
    constructor() {
        this.apiUrl = '/api/StudentHomeBas/messages';
    }


    async sendMessage(messageDTO) {
        const url = `${this.apiUrl}/send`;
        try {
            const response = await axiosInstance.post(url, messageDTO);
            return response.data;
        } catch (error) {
            console.error('Error sending message:', error);
            throw error;
        }
    }


    async fetchMessagesForUser(userId) {
        const url = `${this.apiUrl}/getmessages/${userId}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching messages for user:', error);
            throw error;
        }
    }


    async deleteMessage(messageID) {
        const url = `${this.apiUrl}/delete/${messageID}`;
        try {
            await axiosInstance.delete(url);
            return messageID;
        } catch (error) {
            console.error('Error deleting message:', error);
            throw error;
        }
    }


    async readMessage(messageID) {
        const url = `${this.apiUrl}/read/${messageID}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error reading message:', error);
            throw error;
        }
    }


    async updateMessage(messageDTO) {
        const url = `${this.apiUrl}/update`;
        try {
            const response = await axiosInstance.post(url, messageDTO);
            return response.data;
        } catch (error) {
            console.error('Error updating message:', error);
            throw error;
        }
    }


    async fetchAllMessages() {
        const url = `${this.apiUrl}/getAll`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching all messages:', error);
            throw error;
        }
    }
}


export default new MessageService();
