<template>
  <div class="container custom-background">
    <h2 class="mb-4">Register Property</h2>
    <form @submit="submitProperty">
      <!-- Property Name and Number of Rooms -->
      <div class="row mb-3">
        <div class="col-md-6">
          <label for="propertyName" class="form-label">Property Name</label>
          <input type="text" class="form-control custom-border" id="propertyName" v-model="propertyName" placeholder="Property name" required>
        </div>
        <div class="col-md-6">
          <label for="numRooms" class="form-label" >Number Of Rooms</label>
          <input type="number" class="form-control custom-border" id="numRooms" v-model="numRooms" placeholder="Number of Rooms" required>
        </div>
      </div>

      <!-- Street Name and Suburb -->
      <div class="row mb-3">
        <div class="col-md-6">
          <label for="streetName1" class="form-label">Street Name</label>
          <input type="text" class="form-control custom-border" id="streetName1" v-model="streetName1" placeholder="Street name" required>
        </div>
        <div class="col-md-6">
          <label for="suburb" class="form-label">Suburb</label>
          <input type="text" class="form-control custom-border" id="suburb" v-model="suburb" placeholder="Suburb" required>
        </div>
      </div>

      <!-- City and Postal Code -->
      <div class="row mb-3">
        <div class="col-md-6">
          <label for="city" class="form-label">City</label>
          <input type="text" class="form-control custom-border" id="city" v-model="city" placeholder="City" required>
        </div>
        <div class="col-md-6">
          <label for="postalCode" class="form-label">Postal Code</label>
          <input type="text" class="form-control custom-border" id="postalCode" v-model="postalCode" placeholder="Postal code" required>
        </div>
        <div class="col-md-6">
          <label for="price" class="form-label">Price Per Room</label>
          <input type="text" class="form-control custom-border" id="price" v-model="price" placeholder="Price" required>
        </div>
      </div>

      <!-- Document Upload Section -->
      <div class="mb-3">
        <h4 class="mb-3">Upload Documents</h4>
        <button type="button" class="btn btn-secondary mb-3" @click="toggleDocumentForm">Add Document</button>

        <!-- Dynamic Document Upload Form -->
        <div v-if="showDocumentForm" class="document-form-popup p-3 mb-3">
          <div class="row">
            <div class="col-md-6">
              <label for="documentName" class="form-label">Document Name</label>
              <input type="text" class="form-control" v-model="newDocument.documentName" placeholder="Document name" required>
            </div>
            <div class="col-md-6">
              <label for="fileContents" class="form-label">Upload Document</label>
              <input type="file" class="form-control" @change="handleFileUpload" required>
            </div>
          </div>
          <div v-if="formError" class="text-danger mt-2">{{ formError }}</div>
          <button type="button" class="btn btn-primary mt-3" @click="addDocument">Save Document</button>
        </div>

        <!-- Display Uploaded Documents -->
        <div class="uploaded-documents">
          <h5>Uploaded Documents</h5>
          <div class="d-flex flex-wrap">
            <div v-for="(document, index) in documents" :key="index" class="document-thumbnail position-relative me-2 mb-2">
              <img :src="document.thumbnail" alt="Document Thumbnail" class="img-thumbnail" width="100" height="100">
              <p class="text-center small">{{ document.documentName }}</p>
              <button type="button" class="btn-close remove-icon" aria-label="Close" @click="removeDocument(index)"></button>
            </div>
          </div>
        </div>
      </div>

      <!-- Terms and Submit Button -->
      <div class="form-check mb-4">
        <input class="form-check-input custom-border" type="checkbox" id="termsAndConditions">
        <label class="form-check-label" for="termsAndConditions">
          I read and agree to the <a href="#">Terms and conditions</a>
        </label>
      </div>
      <button type="submit" class="btn text-white custom-button">Submit</button>
    </form>
  </div>
</template>

<script>
import PropertyService from '@/service/PropertyService'; // Adjust the path as needed
import LandlordService from "@/service/LandlordService";
import DocumentService from "@/service/DocumentService";
import Address_Service from "@/service/Address_Service"; // Adjust the path as needed

export default {
  name: 'RegisterProperty',
  data() {
    return {
      showDocumentForm: false,
      newDocument: {
        documentName: '',
        fileContents: null,
        thumbnail: ''
      },
      documents: [],
      formError: '',
      landlord: null,
      propertyName: '',
      numRooms: '',
      streetName1: '',
      suburb: '',
      city: '',
      postalCode: '',
      price: '',
    };
  },
  async mounted() {
    try {
      // Get landlord ID from route params or other source
      // const landlordId = this.$route.params.id; // If using Vue Router
      const landlordId = 4; // Example static ID, replace with actual logic

      if (landlordId) {
        this.landlord = await LandlordService.readLandlord(landlordId);
        // Populate form fields with landlord details if necessary
      }
    } catch (error) {
      console.error('Error fetching landlord data:', error);
    }
  },
  methods: {
    toggleDocumentForm() {
      this.showDocumentForm = !this.showDocumentForm;
      this.newDocument = {documentName: '', fileContents: null, thumbnail: ''};
      this.formError = '';
    },
    handleFileUpload(event) {
      const file = event.target.files[0];

      if (this.documents.some(doc => doc.fileContents && doc.fileContents.name === file.name)) {
        this.formError = 'This file has already been uploaded.';
        this.newDocument.fileContents = null;
        return;
      }

      const reader = new FileReader();
      reader.onload = (e) => {
        // e.target.result contains the base64 encoded string of the file
        this.newDocument.fileContents = e.target.result.split(',')[1]; // Remove "data:image/png;base64," part
        this.newDocument.thumbnail = URL.createObjectURL(file);
        this.formError = '';
      };
      reader.readAsDataURL(file); // Read the file as a data URL (base64 encoded string)
    },
    addDocument() {
      if (!this.newDocument.documentName || !this.newDocument.fileContents) {
        this.formError = 'Both document name and file are required.';
        return;
      }
      this.documents.push({...this.newDocument});
      this.newDocument = {documentName: '', fileContents: null, thumbnail: ''};
      this.showDocumentForm = false;
      this.formError = '';
    },
    removeDocument(index) {
      this.documents.splice(index, 1);
    },
    resetForm() {
      this.propertyName = '';
      this.numRooms = '';
      this.streetName1 = '';
      this.suburb = '';
      this.city = '';
      this.postalCode = '';
      this.price = '';
      this.documents = [];
      this.landlord = null;
    },
    async submitProperty(event) {
      event.preventDefault();

      try {
        // Step 1: Save Address
        const address = {
          addressID : null,
          street: this.streetName1,
          suburb: this.suburb,
          city: this.city,
          postalCode: this.postalCode,
        };

        const savedAddress = await Address_Service.saveAddress(address);
        this.addressId = savedAddress.addressID;

        // Step 2: Save Documents
        const savedDocuments = [];
        for (const doc of this.documents) {
          const document = {
            documentName: doc.documentName,
            fileContents: doc.fileContents, // Assuming this is base64 encoded
          };
          const savedDocument = await DocumentService.saveDocument(document);
          savedDocuments.push(savedDocument);
        }

        // Step 3: Save Property
        const property = {
          propertyName: this.propertyName,
          numberOfRooms: this.numRooms,
          price: this.price,
          address: { addressID: this.addressId }, // Use saved address ID
          landlord: this.landlord,
          pictures: savedDocuments,
        };

        const savedProperty = await PropertyService.saveProperty(property);
        console.log('Property saved successfully:', savedProperty);


        alert('Property saved successfully!');
        this.resetForm();

      } catch (error) {
        console.error('Error saving property:', error);
      }
    },
  }
};
</script>

<style scoped>
.custom-background {
  background-color: #E3E0E0;
  padding: 20px;
  border-radius: 8px;
}

.custom-border {
  border-color: #127670 !important;
}

.custom-button {
  background-color: #127670 !important;
}

.document-form-popup {
  background-color: #f8f9fa;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.uploaded-documents .document-thumbnail {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.uploaded-documents .remove-icon {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  color: red;
  border: none;
  opacity: 1;
}

.uploaded-documents .remove-icon:hover {
  color: white;
  background-color: red;
}
</style>
