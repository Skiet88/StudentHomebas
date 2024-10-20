package za.ac.cput.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Student")
public class Student extends User {

    @OneToOne(cascade = CascadeType.ALL)
    private AcademicDetails academicDetails;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Document> documents;

    protected Student() {}

    private Student(StudentBuilder builder) {
        super(builder);
        this.academicDetails = builder.academicDetails;
        this.documents = builder.documents;
    }

    public AcademicDetails getAcademicDetails() {
        return academicDetails;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        return "Student{" +
                ", userId=" + userId +
                ", name=" + name +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", password='" + password + '\'' +
                ", contact=" + contact +
                "academicDetails=" + academicDetails +
                ", documents=" + documents +
                '}';
    }

    public static class StudentBuilder extends User.UserBuilder<StudentBuilder> {
        private AcademicDetails academicDetails;
        private List<Document> documents;

        public StudentBuilder setAcademicDetails(AcademicDetails academicDetails) {
            this.academicDetails = academicDetails;
            return self();
        }
        public StudentBuilder setDocuments(List<Document> documents) {
            this.documents = documents;
            return self();
        }
        @Override
        public StudentBuilder copy(User user) {
            super.copy(user);
            if (user instanceof Student) {
                this.documents = ((Student) user).documents;
                this.academicDetails = ((Student) user).academicDetails;
            }
            return self();
        }

        @Override
        protected StudentBuilder self() {
            return this;
        }

        @Override
        public Student build() {
            return new Student(this);
        }
    }
}
