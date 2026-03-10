package com.flabs.nodes_crud.Node;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Node {

    protected Node(){

    }
    
    @Id
    @GeneratedValue
    private int nodeId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String input;

    @NotNull
    private String output;
    
    @NotNull    
    private String nodeType;
    
    private float positionX;
    private float positionY;
    private int sequence;
    private boolean is_active;

    @CreationTimestamp
    @Column(updatable = false)
    private ZonedDateTime create_timestamp;

    @UpdateTimestamp
    private ZonedDateTime update_timestamp;

    public Node(
        int nodeId, String name, String description, String input, String output,
        String nodeType, float positionX, float positionY, int sequence, boolean is_active
    ){
        super();
        this.nodeId = nodeId;
        this.name = name;
        this.description = description;
        this.input = input;
        this.output = output;
        this.nodeType = nodeType;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sequence = sequence;
        this.is_active = is_active;
    }

    public int getNodeId(){
        return this.nodeId;
    }

    public void setNodeId(int id){
        this.nodeId = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String nme){
        this.name = nme;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public String getInput(){
        return this.input;
    }

    public void setInput(String inp){
        this.input = inp;
    }

    public String getOutput(){
        return this.output;
    }

    public void setOutput(String out){
        this.output = out;
    }

    public String getNodeType(){
        return this.nodeType;
    }

    public void setNodeType(String ndtype){
        this.nodeType = ndtype;
    }

    public float getPositionX(){
        return this.positionX;
    }

    public void setPositionX(float pos_x){
        this.positionX = pos_x;
    }


    public float getPositionY(){
        return this.positionY;
    }

    public void setPositionY(float pos_y){
        this.positionY = pos_y;
    }

    public int getSequence(){
        return this.sequence;
    }

    public void setSequence(int seq){
        this.sequence = seq;
    }

    public boolean getIsActive(){
        return this.is_active;
    }

    public void setIsActive(boolean activ){
        this.is_active = activ;
    }

    @Override
    public String toString() {
        return "Node{" +
                "node_id=" + nodeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", position_x=" + positionX +
                ", position_y=" + positionY +
                ", sequence=" + sequence +
                ", is_active=" + is_active +
                ", create_timestamp=" + create_timestamp +
                ", update_timestamp=" + update_timestamp +
                '}';
    }

}
