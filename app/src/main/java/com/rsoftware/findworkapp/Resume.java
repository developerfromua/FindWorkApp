package com.rsoftware.findworkapp;

public class Resume {
    private String DocId;
    private String Name;
    private String Surname;
    private String MiddleName;
    private String WantedVacancy;
    private String WantedSalary;
    private String Business;
    private String Schedule;
    private String Phone;
    private String Email;
    private String City;
    private String Citizenship;
    private String Sex;
    private String Education;
    private String WorkExp;
    private String EducationInstitution;
    private String Factuality;
    private String EducationSpeciality;
    private String YearEndingEducation;
    private String EducationForm;
    private String ResumeSkills;

    public Resume(String docId, String name, String surname, String middleName, String wantedVacancy, String wantedSalary, String business, String schedule, String phone, String email, String city, String citizenship, String sex, String education, String workExp, String educationInstitution, String factuality, String educationSpeciality, String yearEndingEducation, String educationForm, String resumeSkills) {
        DocId = docId;
        Name = name;
        Surname = surname;
        MiddleName = middleName;
        WantedVacancy = wantedVacancy;
        WantedSalary = wantedSalary;
        Business = business;
        Schedule = schedule;
        Phone = phone;
        Email = email;
        City = city;
        Citizenship = citizenship;
        Sex = sex;
        Education = education;
        WorkExp = workExp;
        EducationInstitution = educationInstitution;
        Factuality = factuality;
        EducationSpeciality = educationSpeciality;
        YearEndingEducation = yearEndingEducation;
        EducationForm = educationForm;
        ResumeSkills = resumeSkills;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getWantedVacancy() {
        return WantedVacancy;
    }

    public void setWantedVacancy(String wantedVacancy) {
        WantedVacancy = wantedVacancy;
    }

    public String getWantedSalary() {
        return WantedSalary;
    }

    public void setWantedSalary(String wantedSalary) {
        WantedSalary = wantedSalary;
    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        Business = business;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public void setCitizenship(String citizenship) {
        Citizenship = citizenship;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getWorkExp() {
        return WorkExp;
    }

    public void setWorkExp(String workExp) {
        WorkExp = workExp;
    }

    public String getEducationInstitution() {
        return EducationInstitution;
    }

    public void setEducationInstitution(String educationInstitution) {
        EducationInstitution = educationInstitution;
    }

    public String getFactuality() {
        return Factuality;
    }

    public void setFactuality(String factuality) {
        Factuality = factuality;
    }

    public String getEducationSpeciality() {
        return EducationSpeciality;
    }

    public void setEducationSpeciality(String educationSpeciality) {
        EducationSpeciality = educationSpeciality;
    }

    public String getYearEndingEducation() {
        return YearEndingEducation;
    }

    public void setYearEndingEducation(String yearEndingEducation) {
        YearEndingEducation = yearEndingEducation;
    }

    public String getEducationForm() {
        return EducationForm;
    }

    public void setEducationForm(String educationForm) {
        EducationForm = educationForm;
    }

    public String getResumeSkills() {
        return ResumeSkills;
    }

    public void setResumeSkills(String resumeSkills) {
        ResumeSkills = resumeSkills;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }
}
