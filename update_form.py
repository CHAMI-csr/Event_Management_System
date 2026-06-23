import xml.etree.ElementTree as ET
import re

form_path = r'c:\Users\chamika\Documents\NetBeansProjects\Event_Management_System\src\event_management_system\Billing_and_Cost.form'

with open(form_path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Add Transport Cost after txtExtraCost
insert_transport_cost = '''            </Component>
            <Component class=\"javax.swing.JLabel\" name=\"lblTransportCost\">
              <Properties>
                <Property name=\"font\" type=\"java.awt.Font\" editor=\"org.netbeans.beaninfo.editors.FontEditor\">
                  <Font name=\"Segoe UI\" size=\"13\" style=\"1\"/>
                </Property>
                <Property name=\"foreground\" type=\"java.awt.Color\" editor=\"org.netbeans.beaninfo.editors.ColorEditor\">
                  <Color blue=\"ff\" green=\"d2\" red=\"c8\" type=\"rgb\"/>
                </Property>
                <Property name=\"text\" type=\"java.lang.String\" value=\"Supplier Cost (Rs.):\"/>
              </Properties>
              <Constraints>
                <Constraint layoutClass=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout\" value=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription\">
                  <AbsoluteConstraints x=\"16\" y=\"324\" width=\"150\" height=\"34\"/>
                </Constraint>
              </Constraints>
            </Component>
            <Component class=\"javax.swing.JTextField\" name=\"txtTransportCost\">
              <Properties>
                <Property name=\"editable\" type=\"boolean\" value=\"false\"/>
                <Property name=\"font\" type=\"java.awt.Font\" editor=\"org.netbeans.beaninfo.editors.FontEditor\">
                  <Font name=\"Segoe UI\" size=\"13\" style=\"0\"/>
                </Property>
              </Properties>
              <Constraints>
                <Constraint layoutClass=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout\" value=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription\">
                  <AbsoluteConstraints x=\"172\" y=\"324\" width=\"228\" height=\"34\"/>
                </Constraint>
              </Constraints>
            </Component>'''

content = content.replace(
'''            </Component>
            <Component class=\"javax.swing.JLabel\" name=\"jLabel8\">''', 
insert_transport_cost + '''
            <Component class=\"javax.swing.JLabel\" name=\"jLabel8\">''')

# 2. Add New Payment after txtAdvance
insert_new_payment = '''            </Component>
            <Component class=\"javax.swing.JLabel\" name=\"lblNewPayment\">
              <Properties>
                <Property name=\"font\" type=\"java.awt.Font\" editor=\"org.netbeans.beaninfo.editors.FontEditor\">
                  <Font name=\"Segoe UI\" size=\"13\" style=\"1\"/>
                </Property>
                <Property name=\"foreground\" type=\"java.awt.Color\" editor=\"org.netbeans.beaninfo.editors.ColorEditor\">
                  <Color blue=\"82\" green=\"ff\" red=\"82\" type=\"rgb\"/>
                </Property>
                <Property name=\"text\" type=\"java.lang.String\" value=\"New Payment (Rs.):\"/>
              </Properties>
              <Constraints>
                <Constraint layoutClass=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout\" value=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription\">
                  <AbsoluteConstraints x=\"16\" y=\"438\" width=\"150\" height=\"34\"/>
                </Constraint>
              </Constraints>
            </Component>
            <Component class=\"javax.swing.JTextField\" name=\"txtNewPayment\">
              <Properties>
                <Property name=\"font\" type=\"java.awt.Font\" editor=\"org.netbeans.beaninfo.editors.FontEditor\">
                  <Font name=\"Segoe UI\" size=\"13\" style=\"0\"/>
                </Property>
                <Property name=\"text\" type=\"java.lang.String\" value=\"0.00\"/>
              </Properties>
              <Events>
                <EventHandler event=\"keyReleased\" listener=\"java.awt.event.KeyListener\" parameters=\"java.awt.event.KeyEvent\" handler=\"txtNewPaymentKeyReleased\"/>
              </Events>
              <Constraints>
                <Constraint layoutClass=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout\" value=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription\">
                  <AbsoluteConstraints x=\"172\" y=\"438\" width=\"228\" height=\"34\"/>
                </Constraint>
              </Constraints>
            </Component>'''

content = content.replace(
'''            </Component>
            <Component class=\"javax.swing.JLabel\" name=\"jLabel10\">''',
insert_new_payment + '''
            <Component class=\"javax.swing.JLabel\" name=\"jLabel10\">''')

# 3. Update Y coordinates
# jLabel8, txtGrandTotal -> y=362
content = re.sub(r'name=\"jLabel8\">\s*<Properties>.*?</Properties>\s*<Constraints>\s*<Constraint.*?>\s*<AbsoluteConstraints x=\"(.*?)\" y=\"324\"', r'name=\"jLabel8\">\n              <Properties>\n                <Property name=\"font\" type=\"java.awt.Font\" editor=\"org.netbeans.beaninfo.editors.FontEditor\">\n                  <Font name=\"Segoe UI\" size=\"13\" style=\"1\"/>\n                </Property>\n                <Property name=\"foreground\" type=\"java.awt.Color\" editor=\"org.netbeans.beaninfo.editors.ColorEditor\">\n                  <Color blue=\"ff\" green=\"d2\" red=\"c8\" type=\"rgb\"/>\n                </Property>\n                <Property name=\"text\" type=\"java.lang.String\" value=\"Grand Total (Rs.):\"/>\n              </Properties>\n              <Constraints>\n                <Constraint layoutClass=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout\" value=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription\">\n                  <AbsoluteConstraints x=\"\1\" y=\"362\"', content, flags=re.DOTALL)

content = re.sub(r'name=\"txtGrandTotal\">\s*<Properties>.*?</Properties>\s*<Constraints>\s*<Constraint.*?>\s*<AbsoluteConstraints x=\"(.*?)\" y=\"324\"', r'name=\"txtGrandTotal\">\n              <Properties>\n                <Property name=\"editable\" type=\"boolean\" value=\"false\"/>\n                <Property name=\"font\" type=\"java.awt.Font\" editor=\"org.netbeans.beaninfo.editors.FontEditor\">\n                  <Font name=\"Segoe UI\" size=\"14\" style=\"1\"/>\n                </Property>\n              </Properties>\n              <Constraints>\n                <Constraint layoutClass=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout\" value=\"org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription\">\n                  <AbsoluteConstraints x=\"\1\" y=\"362\"', content, flags=re.DOTALL)

# jLabel9, txtAdvance -> y=400
content = re.sub(r'(name=\"jLabel9\".*?<AbsoluteConstraints x=\"16\" y=\")368', r'\g<1>400', content, flags=re.DOTALL)
content = re.sub(r'(name=\"txtAdvance\".*?<AbsoluteConstraints x=\"172\" y=\")368', r'\g<1>400', content, flags=re.DOTALL)

# jLabel10, txtDueBalance -> y=476
content = re.sub(r'(name=\"jLabel10\".*?<AbsoluteConstraints x=\"16\" y=\")412', r'\g<1>476', content, flags=re.DOTALL)
content = re.sub(r'(name=\"txtDueBalance\".*?<AbsoluteConstraints x=\"172\" y=\")412', r'\g<1>476', content, flags=re.DOTALL)

# jLabel11, cmbStatus -> y=514
content = re.sub(r'(name=\"jLabel11\".*?<AbsoluteConstraints x=\"16\" y=\")462', r'\g<1>514', content, flags=re.DOTALL)
content = re.sub(r'(name=\"cmbStatus\".*?<AbsoluteConstraints x=\"172\" y=\")462', r'\g<1>514', content, flags=re.DOTALL)

# btnUpdateStatus, btnGenerate -> y=560
content = re.sub(r'(name=\"btnUpdateStatus\".*?<AbsoluteConstraints x=\"16\" y=\")512', r'\g<1>560', content, flags=re.DOTALL)
content = re.sub(r'(name=\"btnGenerate\".*?<AbsoluteConstraints x=\"198\" y=\")512', r'\g<1>560', content, flags=re.DOTALL)

with open(form_path, 'w', encoding='utf-8') as f:
    f.write(content)
print('Form updated successfully.')
