package com.pm.patient_service.service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
 private PatientRepository patientRepository;
 private BillingServiceGrpcClient billingServiceGrpcClient;
 public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
     this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
 }



 public List<PatientResponseDTO> getPatients(){
     List<Patient> patients = patientRepository.findAll();
    List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
     return patientResponseDTOS;
 }

 public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
     if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
         throw new EmailAlreadyExistsException("A patient with this email"+"already exists"+ patientRequestDTO.getEmail());
     }

     Patient patient=PatientMapper.toPatient(patientRequestDTO);
     Patient newPatient = patientRepository.save(patient);
     billingServiceGrpcClient.createBillingAccount(String.valueOf(newPatient.getId()), newPatient.getName(), newPatient.getEmail());
        return PatientMapper.toPatientResponseDTO(newPatient);
 }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
    Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with ID :"+id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)){
            throw new EmailAlreadyExistsException("A patient with this email"+"already exists"+ patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patient.getDateOfBirth());

        Patient updaedPatient = patientRepository.save(patient);
         return PatientMapper.toPatientResponseDTO(updaedPatient);
 }
 public void deletePatient(UUID id){
     patientRepository.deleteById(id);
 }

}
