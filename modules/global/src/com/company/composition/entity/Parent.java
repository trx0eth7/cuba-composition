package com.company.composition.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "COMPOSITION_PARENT")
@Entity(name = "composition_Parent")
@NamePattern("%s|name")
public class Parent extends StandardEntity {
    private static final long serialVersionUID = -4839480218210706442L;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "parent")
    private List<Child> childs;

    public List<Child> getChilds() {
        return childs;
    }

    public void setChilds(List<Child> childs) {
        this.childs = childs;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}