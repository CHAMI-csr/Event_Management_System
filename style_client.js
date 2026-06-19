const fs = require('fs');
let content = fs.readFileSync('src/event_management_system/client_Details.form', 'utf8');

// Update JPanel backgrounds
content = content.replace(
  /<Property name="background" type="java\.awt\.Color" editor="org\.netbeans\.beaninfo\.editors\.ColorEditor">\s*<Color blue="24" green="1a" red="1a" type="rgb"\/>\s*<\/Property>/g,
  '<Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n              <Color blue="26" green="18" red="18" type="rgb"/>\n            </Property>'
);

// Style all JTextFields
content = content.replace(
  /<Component class="javax\.swing\.JTextField" name="([^"]+)">\s*<Properties>([\s\S]*?)<\/Properties>/g,
  (match, name, innerProps) => {
    let p = innerProps.replace(/<Property name="border"[\s\S]*?<\/Property>/, '');
    p = p.replace(/<Property name="background"[\s\S]*?<\/Property>/, '');
    p = p.replace(/<Property name="foreground"[\s\S]*?<\/Property>/, '');
    p = p.replace(/<Property name="caretColor"[\s\S]*?<\/Property>/, '');
    
    return `<Component class="javax.swing.JTextField" name="${name}">\n              <Properties>${p}\n                <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n                  <Color blue="33" green="33" red="33" type="rgb"/>\n                </Property>\n                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n                  <Color blue="ff" green="ff" red="ff" type="rgb"/>\n                </Property>\n                <Property name="caretColor" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n                  <Color blue="ff" green="ff" red="ff" type="rgb"/>\n                </Property>\n                <Property name="border" type="javax.swing.border.Border" editor="org.netbeans.modules.form.editors2.BorderEditor">\n                  <Border info="org.netbeans.modules.form.compat2.border.EmptyBorderInfo">\n                    <EmptyBorder bottom="5" left="5" right="5" top="5"/>\n                  </Border>\n                </Property>\n              </Properties>`;
  }
);

// Style JTable
content = content.replace(
  /(<Component class="javax\.swing\.JTable" name="jTableClients">\s*<Properties>)/s,
  `$1\n                    <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n                      <Color blue="33" green="33" red="33" type="rgb"/>\n                    </Property>\n                    <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n                      <Color blue="ff" green="ff" red="ff" type="rgb"/>\n                    </Property>\n                    <Property name="gridColor" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n                      <Color blue="66" green="66" red="66" type="rgb"/>\n                    </Property>\n                    <Property name="rowHeight" type="int" value="30"/>`
);

// Style Labels font
content = content.replace(
  /<Font name="Segoe UI Black" size="18" style="1"\/>/g,
  '<Font name="Segoe UI" size="16" style="1"/>'
);

// Style btnAddClient
content = content.replace(
  /(<Component class="javax\.swing\.JButton" name="btnAddClient">\s*<Properties>[\s\S]*?<Property name="background" type="java\.awt\.Color" editor="org\.netbeans\.beaninfo\.editors\.ColorEditor">\s*)<Color blue="66" green="0" red="33" type="rgb"\/>/s,
  '$1<Color blue="f0" green="78" red="5a" type="rgb"/>'
);

// Style btnUpdateClient
content = content.replace(
  /(<Component class="javax\.swing\.JButton" name="btnUpdateClient">\s*<Properties>[\s\S]*?<Property name="background" type="java\.awt\.Color" editor="org\.netbeans\.beaninfo\.editors\.ColorEditor">\s*)<Color blue="ff" green="33" red="0" type="rgb"\/>/s,
  '$1<Color blue="b3" green="66" red="0" type="rgb"/>'
);

// Style btnDelClient
content = content.replace(
  /(<Component class="javax\.swing\.JButton" name="btnDelClient">\s*<Properties>[\s\S]*?<Property name="background" type="java\.awt\.Color" editor="org\.netbeans\.beaninfo\.editors\.ColorEditor">\s*)<Color blue="33" green="0" red="ff" type="rgb"\/>/s,
  '$1<Color blue="33" green="0" red="cc" type="rgb"/>'
);

// Style btnCancelClient
content = content.replace(
  /(<Component class="javax\.swing\.JButton" name="btnCancelClient">\s*<Properties>[\s\S]*?<Property name="background" type="java\.awt\.Color" editor="org\.netbeans\.beaninfo\.editors\.ColorEditor">\s*)<Color blue="7f" green="0" red="ff" type="rgb"\/>/s,
  '$1<Color blue="33" green="33" red="33" type="rgb"/>'
);

fs.writeFileSync('src/event_management_system/client_Details.form', content);
