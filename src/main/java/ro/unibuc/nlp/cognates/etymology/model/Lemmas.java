//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs 
// See <a href="http://java.sun.com/xml/model">http://java.sun.com/xml/model</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.18 at 07:21:32 PM EET 
//


package ro.unibuc.nlp.cognates.etymology.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Lemmas element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="lemmas">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="lemma" type="{}lemma" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "lemma"
})
@XmlRootElement(name = "lemmas")
public class Lemmas {

    @XmlElement(required = true)
    protected List<Lemma> lemma;

    /**
     * Gets the value of the lemma property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lemma property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLemma().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lemma }
     * 
     * 
     */
    public List<Lemma> getLemma() {
        if (lemma == null) {
            lemma = new ArrayList<Lemma>();
        }
        return this.lemma;
    }

}
