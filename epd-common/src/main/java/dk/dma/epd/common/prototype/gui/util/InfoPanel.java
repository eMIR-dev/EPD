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
package dk.dma.epd.common.prototype.gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.bbn.openmap.omGraphics.OMGraphic;

/**
 * Abstract base class for panels to be shown on the map in the glass pane
 */
public abstract class InfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected JLabel textLabel;
    private JLabel imageLabel;
    private double mouseDist;

    /**
     * Constructor
     */
    public InfoPanel() {
        super();
        FlowLayout flowLayout = new FlowLayout();
        setLayout(flowLayout);
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
//        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(30, 30, 30), new Color(45, 45, 45)));
        textLabel = new JLabel();
        add(textLabel);
        setVisible(false);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        textLabel.setBackground(new Color(83, 83, 83));
        textLabel.setForeground(new Color(237, 237, 237));
//        setBackground(new Color(83, 83, 83));
        setBackground(new Color(83,83,83,200));
    }

    /**
     * Constructor with an image
     * @param image
     */
    public InfoPanel(ImageIcon image) {
        super();
        imageLabel =  new JLabel(image);
        FlowLayout flowLayout = new FlowLayout();
        setLayout(flowLayout);
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        this.setBackground(null);
        this.setBorder(null);
        this.setOpaque(false);
        add(imageLabel);
        setVisible(false);
    }

    /**
     * Resize and display label
     */
    public void resizeAndShow() {
        validate();
        Dimension d = textLabel.getSize();
        this.setSize(d.width + 6, d.height + 4);
    }

    /**
     * Set position of element
     * @param x location
     * @param y location
     */
    public void setPos(int x, int y) {
        Rectangle rect = getBounds();
        setBounds(x, y, (int) rect.getWidth(), (int) rect.getHeight());
    }

    /**
     * Show the image
     */
    public void showImage(){
        validate();
        Dimension d = imageLabel.getSize();
        this.setSize(d.width, d.height);
    }

    /**
     * Show text
     * @param text
     */
    public void showText(String text) {
        textLabel.setText(text);
        resizeAndShow();
    }

    /**
     * Returns the distance from the current cursor position to the graphics associated with the info panel
     * @return the distance from the current cursor position to the graphics associated with the info panel
     */
    public double getMouseDist() {
        return mouseDist;
    }

    /**
     * Sets the distance from the current cursor position to the graphics associated with the info panel
     * @param mouseDist the distance from the current cursor position to the graphics associated with the info panel
     */
    public void setMouseDist(double mouseDist) {
        this.mouseDist = mouseDist;
    }

    /**
     * Defines a binding between {@linkplain OMGraphic} classes and {@code InfoPanel}
     */
    public static class InfoPanelBinding {
        
        /**
         * For the mapping, we use a linked hash map to preserve the order
         * of the graphic elements
         */
        Map<Class<?>, InfoPanel> binding = new LinkedHashMap<>();
        Class<?>[] graphicsList;
        
        /**
         * Add the binding between {@linkplain OMGraphic} classes and {@code InfoPanel}
         * 
         * @param infoPanel the {@code InfoPanel}
         * @param graphics the list of {@linkplain OMGraphic} classes bound to the InfoPanel
         */
        @SafeVarargs
        public final synchronized void addBinding(InfoPanel infoPanel, Class<?>... graphics) {
            Objects.requireNonNull(infoPanel, "InfoPanel must be defined");
            Objects.requireNonNull(graphics, "The OMGraphic list must be defined");
            
            // Add the bindings
            for (Class<?> clazz : graphics) {
                if (binding.containsKey(clazz)) {
                    throw new IllegalArgumentException("The class " + clazz + " is already defined");
                }
                binding.put(clazz, infoPanel);
            }
            
            // Invalidate the graphics list
            graphicsList = null;
        }
        
        /**
         * Returns the total list of {@linkplain OMGraphic} classes
         * @return the total list of {@linkplain OMGraphic} classes
         */
        public synchronized Class<?>[] getGraphicsList() {
            if (graphicsList == null) {
                graphicsList =  binding.keySet().toArray(new Class<?>[binding.size()]);
            }
            return graphicsList;
        }
        
        /**
         * Returns the info panel associated with the given {@linkplain OMGraphic} class
         * @param clazz the {@linkplain OMGraphic} class
         * @return the associated info panel, or null if none is defined
         */
        public synchronized InfoPanel getInfoPanel(Class<? extends OMGraphic> clazz) {
            for (Class<?> g : binding.keySet()) {
                if (g.isAssignableFrom(clazz)) {
                    return binding.get(g);
                }
            }
            return null;
        }
        
        /**
         * Returns the total list of info panels
         * @return the total list of info panels
         */
        public synchronized Collection<InfoPanel> getInfoPanels() {
            return binding.values();
        }
        
        /**
         * Returns if no binding is defined
         * @return if no binding is defined
         */
        public synchronized boolean isEmpty() {
            return binding.size() == 0;
        }
    }
}
