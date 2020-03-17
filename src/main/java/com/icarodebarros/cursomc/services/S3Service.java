package com.icarodebarros.cursomc.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.chime.model.AmazonChimeException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3Client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	public void uploadFile(String localFilePath) {
		try {
			File file = new File(localFilePath);
			LOG.info("Iniciando upload");
			s3Client.putObject(new PutObjectRequest(bucketName, "teste.png", file));
			LOG.info("Upload finalizado");
			
		} catch (AmazonS3Exception e) {
			LOG.info("AmazonS3Exception: " + e.getErrorMessage());
			LOG.info("Status code: " + e.getErrorCode());
			
		} catch (AmazonChimeException e) {
			LOG.info("AmazonChimeException: " + e.getErrorMessage());
		}
			
	}

}
