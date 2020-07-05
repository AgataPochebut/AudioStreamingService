//package com.epam.songservice.service.repository;
//
//import com.epam.songservice.repository.ResourceRepository;
//import com.epam.songservice.model.Resource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//public class ResourceRepositoryServiceImpl extends GenericServiceImpl<Resource, Long> implements ResourceRepositoryService {
//
//    @Autowired
//    private ResourceRepository repository;
//
//    @Override
//    public Resource findByChecksum(String s) {
//        return repository.findByChecksum(s).orElse(null);
//    }
//
//    @Override
//    public boolean existByChecksum(String s){
//        return !repository.findByChecksum(s).isEmpty();
//    }
//}
