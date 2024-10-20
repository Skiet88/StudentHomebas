//import axiosInstance from './axiosInstance'; // Path to the axiosInstance file
import axiosInstance from 'axios';

class DocumentService {
    constructor() {
        this.apiUrl = '/api/StudentHomeBas/document';
    }


    async saveDocument(document) {
        const url = `${this.apiUrl}/save`;
        try {
            const response = await axiosInstance.post(url, document);
            return response.data;
        } catch (error) {
            console.error('Error saving document:', error);
            throw error;
        }
    }


    async readDocument(documentID) {
        const url = `${this.apiUrl}/read/${documentID}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error reading document:', error);
            throw error;
        }
    }


    async updateDocument(document) {
        const url = `${this.apiUrl}/update`;
        try {
            const response = await axiosInstance.put(url, document);
            return response.data;
        } catch (error) {
            console.error('Error updating document:', error);
            throw error;
        }
    }


    async deleteDocument(documentID) {
        const url = `${this.apiUrl}/delete/${documentID}`;
        try {
            const response = await axiosInstance.delete(url);
            return response.data;
        } catch (error) {
            console.error('Error deleting document:', error);
            throw error;
        }
    }


    async fetchAllDocuments() {
        const url = `${this.apiUrl}/getall`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching documents:', error);
            throw error;
        }
    }
}

export default new DocumentService();
