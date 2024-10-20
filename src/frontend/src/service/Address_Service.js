//import axiosInstance from './axiosInstance'; // Path to the axiosInstance file
import axiosInstance from 'axios';

class AddressService {
    constructor() {
        this.apiUrl = '/api/StudentHomeBas/address';
    }


    async saveAddress(address) {
        const url = `${this.apiUrl}/save`;
        try {
            const response = await axiosInstance.post(url, address);
            return response.data;
        } catch (error) {
            console.error('Error saving address:', error);
            throw error;
        }
    }


    async readAddress(addressID) {
        const url = `${this.apiUrl}/read/${addressID}`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error reading address:', error);
            throw error;
        }
    }


    async updateAddress(address) {
        const url = `${this.apiUrl}/update`;
        try {
            const response = await axiosInstance.put(url, address);
            return response.data;
        } catch (error) {
            console.error('Error updating address:', error);
            throw error;
        }
    }


    async deleteAddress(addressID) {
        const url = `${this.apiUrl}/delete/${addressID}`;
        try {
            const response = await axiosInstance.delete(url);
            return response.data;
        } catch (error) {
            console.error('Error deleting address:', error);
            throw error;
        }
    }


    async fetchAllAddresses() {
        const url = `${this.apiUrl}/getall`;
        try {
            const response = await axiosInstance.get(url);
            return response.data;
        } catch (error) {
            console.error('Error fetching addresses:', error);
            throw error;
        }
    }
}

export default new AddressService();
