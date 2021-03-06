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
package dk.dma.epd.ship.gui.component_panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import dk.dma.epd.ship.EPDShip;
public class ShowDockableDialog extends JDialog implements ActionListener {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    JLabel lblDockable;
    JButton yesButton;
    JButton noButton;
    JCheckBox chckbxNewCheckBox;
    dock_type type;
    
    public enum dock_type {
        ROUTE, DYN_NOGO, NOGO,AIS
    }

    /**
     * Create the dialog.
     */
    public ShowDockableDialog(JFrame parent, dock_type type) {
        super(parent, "Activate Dock", true);
        
        setSize(318, 125);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        this.type = type;
        
        initGUI();

        
        if (type == dock_type.ROUTE){
            lblDockable.setText("a route");
        }
        if (type == dock_type.DYN_NOGO){
            lblDockable.setText("Dynamic NoGo");
        }
        if (type == dock_type.NOGO){
            lblDockable.setText("NoGo");
        }
        if (type == dock_type.AIS){
            lblDockable.setText("an AIS Target");
        }
        
        
        setVisible(true);
    }

    private void initGUI() {

        JPanel contentPanel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            JLabel lblYouHaveActivated = new JLabel("You have activated");
            lblYouHaveActivated.setBounds(10, 11, 93, 14);
            contentPanel.add(lblYouHaveActivated);
        }
        
        lblDockable = new JLabel("N/A");
        lblDockable.setBounds(105, 11, 150, 14);
        contentPanel.add(lblDockable);
        
        JLabel lblDoYouWish = new JLabel("Do you wish to activate the dockable panel for it?");
        lblDoYouWish.setBounds(10, 27, 282, 14);
        contentPanel.add(lblDoYouWish);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                yesButton = new JButton("Yes");
                yesButton.setHorizontalAlignment(SwingConstants.LEFT);
                buttonPane.add(yesButton);
                getRootPane().setDefaultButton(yesButton);
                
                yesButton.addActionListener(this);
            }
            {
                noButton = new JButton("No");
                buttonPane.add(noButton);
                
                noButton.addActionListener(this);
            }
            
            chckbxNewCheckBox = new JCheckBox("<html>Always do this and<br>Do not show this message again</html>");

            buttonPane.add(chckbxNewCheckBox);
        }
        
        
        
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getSource() == yesButton){

            //Add it
            if (this.type == dock_type.ROUTE){
                EPDShip.getInstance().getMainFrame().getDockableComponents().openDock("Active Waypoint");
                EPDShip.getInstance().getMainFrame().getJMenuBar().refreshDockableMenu();
            }
            
            if (this.type == dock_type.DYN_NOGO){
                EPDShip.getInstance().getMainFrame().getDockableComponents().openDock("Dynamic NoGo");
                EPDShip.getInstance().getMainFrame().getJMenuBar().refreshDockableMenu();
            }
            
            
            if (this.type == dock_type.NOGO){
                EPDShip.getInstance().getMainFrame().getDockableComponents().openDock("NoGo");
                EPDShip.getInstance().getMainFrame().getJMenuBar().refreshDockableMenu();
            }
            
            if (this.type == dock_type.AIS){
                EPDShip.getInstance().getMainFrame().getDockableComponents().openDock("AIS Target");
                EPDShip.getInstance().getMainFrame().getJMenuBar().refreshDockableMenu();
            }
            
            

            if (chckbxNewCheckBox.isSelected()){
                EPDShip.getInstance().getSettings().getGuiSettings().setAlwaysOpenDock(true);
                EPDShip.getInstance().getSettings().getGuiSettings().setShowDockMessage(false);
                //Do not show again
            }
        }
        
        if(arg0.getSource() == noButton){
            //do nothing
            if (chckbxNewCheckBox.isSelected()){
                EPDShip.getInstance().getSettings().getGuiSettings().setAlwaysOpenDock(false);
                EPDShip.getInstance().getSettings().getGuiSettings().setShowDockMessage(false);
            }
        }
    
        this.dispose();
    }
    
    
    

}


