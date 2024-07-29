package com.mahansa.jobms.job.impl;

import com.mahansa.jobms.job.Job;
import com.mahansa.jobms.job.JobRepository;
import com.mahansa.jobms.job.JobService;
import com.mahansa.jobms.job.clients.CompanyClient;
import com.mahansa.jobms.job.clients.ReviewClient;
import com.mahansa.jobms.job.dto.JobDTO;
import com.mahansa.jobms.job.external.Company;
import com.mahansa.jobms.job.external.Review;
import com.mahansa.jobms.job.mapper.JobMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;
    JobRepository jobRepository;

    int retryAttempts = 0;

    public JobServiceImpl(JobRepository jobRepository,
                          CompanyClient companyClient,
                          ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
//    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
//    @Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    // @RateLimiter(name = "companyBreaker")
    public List<JobDTO> findAll() {
        System.out.println("Attempt: " + retryAttempts++);
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job) {
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

        return JobMapper.mapToJobDTO(job, company, reviews);
    }

    public List<String> companyBreakerFallback() {
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return null;
        }
        return convertToDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}
