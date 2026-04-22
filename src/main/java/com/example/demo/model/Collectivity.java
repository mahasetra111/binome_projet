
package com.example.demo.model;

import java.util.List;
import java.util.UUID;

public class Collectivity {
    private UUID id;
    private String location;
    private Integer number;
    private String name;
    private CollectivityStructure structure;
    private List<Member> members;

    public Collectivity() {}
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public CollectivityStructure getStructure() { return structure; }
    public void setStructure(CollectivityStructure structure) { this.structure = structure; }

    public List<Member> getMembers() { return members; }
    public void setMembers(List<Member> members) { this.members = members; }
}