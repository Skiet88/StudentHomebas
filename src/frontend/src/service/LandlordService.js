//import axiosInstance from './axiosInstance';

import axiosInstance from 'axios';
class LandlordService {
    constructor() {
        this.apiUrl = '/api/StudentHomeBas/landlord'; // Base URL for the Landlord API
    }

    async deleteLandlord(landlordId) {
        const url = `${this.apiUrl}/delete/${landlordId}`;
        try {
            await axiosInstance.delete(url);
            return landlordId; // Return the landlord ID to update state
        } catch (error) {
            console.error('Error deleting landlord:', error);
            throw error;
        }
    }

    async readLandlord(landlordId) {
        const url = `${this.apiUrl}/read/${landlordId}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error reading landlord:', error);
            throw error;
        }
    }

    async saveLandlord(landlord) {
        const url = `${this.apiUrl}/save`;
        try {
            const response = await axiosInstance.post(url, landlord);
            return response.data;
        } catch (error) {
            console.error('Error saving landlord:', error);
            throw error;
        }
    }

    async updateLandlord(landlord) {
        const url = `${this.apiUrl}/update`;
        try {
            const response = await axiosInstance.put(url, landlord);
            return response.data;
        } catch (error) {
            console.error('Error updating landlord:', error);
            throw error;
        }
    }

    async fetchAllLandlords() {
        const url = `${this.apiUrl}/getall`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching all landlords:', error);
            throw error;
        }
    }
}

export default new LandlordService();
