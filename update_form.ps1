 = 'src\event_management_system\manage_Bookings.form'
 = Get-Content -Path  -Raw -Encoding UTF8

# 1. Add background to jPanel1
$content = $content -replace '(<Container class="javax.swing.JPanel" name="jPanel1">\s*<Properties>)', "${1}
        <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
          <Color blue="1c" green="1a" red="1a" type="rgb"/>
        </Property>"

# 2. Update background for jPanel2 and jPanel3
$content = $content -replace '<Color blue="24" green="1a" red="1a" type="rgb"/>', '<Color blue="26" green="18" red="18" type="rgb"/>'

# 3. Update title jLabel1
$content = $content -replace '<Font name="Serif" size="36" style="1"/>', '<Font name="Segoe UI" size="22" style="1"/>
                </Property>
                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="ff" green="e6" red="e6" type="rgb"/>'

# 4. Update NextBid label (needs accent color)
$content = $content -replace '(<Component class="javax.swing.JLabel" name="NextBid">.*?<Font name="Segoe UI" size=)"18" style="1"(/>)', "${1}"16" style="1"$2
                </Property>
                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="f0" green="78" red="5a" type="rgb"/>"

# 5. Update other Labels (jLabel2 to jLabel17). They all have size="18" style="1"
# We match <Component class="javax.swing.JLabel" name="jLabel\d+"> and inject foreground
# Wait, replacing across lines in powershell with -replace:
$patternLabel = '(<Component class="javax.swing.JLabel" name="jLabel\d+">.*?<Font name="Segoe UI" size=)"18"( style="1"/>)'
$content = $content -replace $patternLabel, "${1}"14"$2
                </Property>
                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="ff" green="e6" red="e6" type="rgb"/>"

# 6. Update text fields, comboboxes, spinners (JTextField, JComboBox, JSpinner)
# They currently have font size 18 style 1. Change to size 13 style 0
$patternField = '(<Component class="javax.swing.(?:JTextField|JComboBox|JSpinner)" name=".*?">.*?<Font name="Segoe UI" size=)"18" style="1"(/>)'
$content = $content -replace $patternField, "${1}"13" style="0"$2"

# 7. Update JDateChooser (txtEventDate)
$patternDate = '(<Component class="com.toedter.calendar.JDateChooser" name="txtEventDate">.*?<Font name="Segoe UI" size=)"18" style="1"(/>)'
$content = $content -replace $patternDate, "${1}"13" style="0"$2"

# 8. Update Buttons (jButton1, btnCustomize)
# Currently have font size 18 style 1. We change to size 14 style 1, and add background/foreground
# btnCustomize
$content = $content -replace '(<Component class="javax.swing.JButton" name="btnCustomize">.*?<Property name="text" type="java.lang.String" value="custamize "/>)', "${1}
                <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="1e" green="82" red="c8" type="rgb"/>
                </Property>
                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="ff" green="ff" red="ff" type="rgb"/>
                </Property>"

# jButton1
$content = $content -replace '(<Component class="javax.swing.JButton" name="jButton1">.*?<Font name="Segoe UI" size=)"18"( style="1"/>)', "${1}"14"$2
                </Property>
                <Property name="background" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="22" green="8b" red="22" type="rgb"/>
                </Property>
                <Property name="foreground" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">
                  <Color blue="ff" green="ff" red="ff" type="rgb"/>"

Set-Content -Path $path -Value $content -Encoding UTF8
Write-Output "Script execution finished"
