/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.dma.epd.common.prototype.monalisa.sspa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NGNodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NGNodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ngposition" type="{http://www.sspa.se/voyage-optimizer}NGPositionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGNodeType", propOrder = {
    "ngposition"
})
public class NGNodeType {

    @XmlElement(required = true)
    protected NGPositionType ngposition;

    /**
     * Gets the value of the ngposition property.
     * 
     * @return
     *     possible object is
     *     {@link NGPositionType }
     *     
     */
    public NGPositionType getNgposition() {
        return ngposition;
    }

    /**
     * Sets the value of the ngposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link NGPositionType }
     *     
     */
    public void setNgposition(NGPositionType value) {
        this.ngposition = value;
    }

}
