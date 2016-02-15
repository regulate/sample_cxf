package cxf.sample.persistence.dto

import groovy.transform.Canonical

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElementRef
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by IPotapchuk on 2/15/2016.
 */
@Canonical
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class PersonsCollectionDTO {

    @XmlElementWrapper(name = "users")
    @XmlElementRef
    Collection<PersonDTO> persons

}
