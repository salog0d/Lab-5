package com.flabs.nodes_crud.Node;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class NodeResource {

    private NodeRepository nodeRepository;

    public NodeResource(NodeRepository nodeRepository){
        this.nodeRepository = nodeRepository;
    }
    
    @GetMapping(path = "/api/nodes/{id}")
    public EntityModel<Node> getById(@PathVariable int id){
        Optional<Node> node = nodeRepository.findById(id);
        if(node.isEmpty()){
            throw new NodeNotFoundException("id: " + id);
        }
        EntityModel<Node> entityModel = EntityModel.of(node.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getNodes());
        entityModel.add(link.withRel("all-nodes"));
        return entityModel;
    }

    @DeleteMapping(path = "/api/nodes/{id}")
    public void deleteById(@PathVariable int id){
        Optional<Node> node = nodeRepository.findById(id);
        if(node.isEmpty()){
            throw new NodeNotFoundException("id: " + id);
        }
        nodeRepository.delete(node.get());
    }

    @GetMapping(path = "/api/nodes")
    public List<Node> getNodes(){
        return nodeRepository.findAll();
    }

    @PostMapping(path = "/api/nodes")
    public ResponseEntity<Node> createNode(@Valid @RequestBody Node node){
        Node saved_node = nodeRepository.save(node);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved_node.getNodeId())
            .toUri();
        return ResponseEntity.created(location).build();
    }
}
