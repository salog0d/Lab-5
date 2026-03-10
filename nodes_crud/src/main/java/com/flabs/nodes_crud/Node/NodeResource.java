package com.flabs.nodes_crud.Node;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;

import org.hibernate.grammars.hql.HqlParser.EntityIdExpressionContext;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeResource {

    private NodeRepository nodeRepository;

    public NodeResource(NodeRepository nodeRepository){
        this.nodeRepository = nodeRepository;
    }
    
    @GetMapping(path = "api/nodes/{id}")
    public EntityModel<Node> getById(@PathVariable int id){
        Optional<Node> node = nodeRepository.findById(id);
        if(node.isEmpty()){
            throw new NodeNotFoundException("id: " + id);
        }
        EntityModel<Node> entityModel = EntityModel.of(node.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAll());
        entityModel.add(link.withRel("all-nodes"));
        return entityModel;
    }

    @DeleteMapping(path = "api/nodes/{id}")
    public void deleteById(@PathVariable int id){
        Optional<Node> node = nodeRepository.findById(id);
        if(node.isEmpty()){
            throw new NodeNotFoundException("id: " + id);
        }
        nodeRepository.delete(node.get());
    }
}
