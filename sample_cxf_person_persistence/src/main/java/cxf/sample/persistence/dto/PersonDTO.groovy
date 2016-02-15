package cxf.sample.persistence.dto

import groovy.transform.Canonical
import groovy.transform.builder.Builder

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.sql.Date

/**
 * Created by IPotapchuk on 2/15/2016.
 */
@Canonical
@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class PersonDTO {
    @XmlAttribute
    Long id
    @XmlAttribute
    String firstName
    @XmlAttribute
    String lastName
    @XmlAttribute
    Integer age
    @XmlAttribute
    Date birthDate
}
