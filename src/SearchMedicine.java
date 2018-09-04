import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.sql.*;
import java.util.Date;
import java.util.GregorianCalendar;

public class SearchMedicine extends AbstractMedicine{
	private JInternalFrame jf;
	private String[] buttonName={"Search","Clear","All"}; 
	private String medicineType=null;
	private JComboBox msname,tabtype;
	private String s,sid1,tabt;
	private Date date1;
	private GregorianCalendar calendar;
	private String strDate;
	private PreparedStatement ps;

	public SearchMedicine(){
		jf= new JInternalFrame( 
				"Search Medicine", true, true, true, true );
		jf.setLayout(null);

		ln=new JLabel("Search Medicine");
		ln.setFont(new Font("Times New Roman",Font.BOLD,40));
		ln.setForeground(Color.black);
		ln.setBounds(300,30,400,40);
		jf.add(ln);

		int k=100;
		for(int i=0; i<labelNames.length-6; i++){
			if(i==0 || i==1){
				label[i] = new Label(labelNames[i]+"*");	
			}else{
				label[i] = new Label(labelNames[i]);
			}
			label[i].setBounds(50,k,200,25);
			label[i].setFont(font);
			jf.add(label[i]);
			textfield[i] = new MyTextField(20);
			textfield[i].setBounds(250,k,120,25);
			//t1.setToolTipText("Enter medicine batch no");
			jf.add(textfield[i]);
			k+=40;	
		}

		k=100;
		for(int i=6; i<labelNames.length; i++){
			label[i] = new Label(labelNames[i]);		
			label[i].setBounds(470,k,200,25);
			label[i].setFont(font);
			jf.add(label[i]);

			if(i==6){
				tabtype=new JComboBox();
				for(int j=0; j<comboxName.length; j++){
					tabtype.addItem(comboxName[j]);
				}
				tabtype.setBounds(700,k,120,25);
				tabtype.setToolTipText("Select medicine type");
				jf.add(tabtype);
				tabtype.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						medicineType =(String)tabtype.getSelectedItem();
					}
				});
				jf.add(tabtype);
			}
			else {
				textfield[i] = new MyTextField(20);
				textfield[i].setBounds(700,k,120,25);
				jf.add(textfield[i]);	
			}			
			k+=40;
		}
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(url,user,password);
			System.out.println("Connected to database.");
			ps=con.prepareStatement("select sname from supplier");
			rs=ps.executeQuery();
			while(rs.next()){
				String sname1=rs.getString(1);
				msname.addItem(sname1);
			}
			con.close();
		}
		catch(SQLException se){
			System.out.println(se);
		}
		catch(Exception e){
			System.out.println(e);
		}
		// Buttons Loading...
		int j=250;
		for(int i=0; i<buttonName.length; i++){
			if(i==0){
				buttons[i]=new JButton(buttonName[i],new ImageIcon("images//save.png"));
			}else{
				buttons[i]=new JButton(buttonName[i],new ImageIcon(buttonsImages[i]));	
			}
			buttons[i].setBounds(j,330,110,35);
			buttons[i].addActionListener(this);
			jf.add(buttons[i]);
			j+=150;

		}
		scrlPane.setBounds(0,380,900,600);
		jf.add(scrlPane);
		tableGrid.setFont(font);

		for(int i=0; i<SIZE; i++){
			modelTable.addColumn(modelTableLabels[i]);
		}
		jf.setTitle("Search Medicine ");
		jf.setSize(910,700);
		jf.setLocation(290,10);
		refference.outerPane.add(jf);
		jf.setResizable(true);
		jf.getContentPane().setBackground(Color.green);
		jf.setVisible(true);
	}

	public boolean checkEmpty(){
		if(textfield[0].getText().equals("") || textfield[1].getText().equals("")){
			return false;
		}
		return true;
	}

	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==buttons[0]){
			try{
				if(!this.checkEmpty()){
					JOptionPane.showMessageDialog(null,"Please enter medicine bno or name !","Warning!!!",JOptionPane.WARNING_MESSAGE);
				}
				else{
					int foundrec = 0;
					Class.forName("com.mysql.jdbc.Driver");
					con=DriverManager.getConnection(url,user,password);
					System.out.println("Connected to database.");

					ps=con.prepareStatement("select * from medicine where mname='"+textfield[1].getText()+"' or mbno='"+textfield[0].getText()+"'");
					rs=ps.executeQuery();
					while(rs.next()){
						for(int i=0; i<textfield.length; i++){
							textfield[i].setText(rs.getString(i));
						}
						foundrec = 1;
					}
					if (foundrec == 0){
						JOptionPane.showMessageDialog(null,"Record is not available","Dialog",JOptionPane.WARNING_MESSAGE);
					}
				}
				con.close();
			}
			catch(SQLException se)
			{
				System.out.println(se);
				JOptionPane.showMessageDialog(null,"SQL Error."+se);
			}
			catch(Exception e)
			{
				System.out.println(e);
				JOptionPane.showMessageDialog(null,"Error."+e);
			}
		}
		else if(ae.getSource()==buttons[1]){
			for(int i=0; i<textfield.length; i++){
				textfield[i].setText("");
			}
		}
		else if(ae.getSource()==buttons[2]){
			int r = 0;
			try{
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection(url,user,password);
				System.out.println("Connected to database.");
				stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery("SELECT * from medicine" );
				while(rs.next()){
					modelTable.insertRow(r++, new Object[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12) });
				}
				con.close();
			}
			catch(SQLException se){
				System.out.println(se);
				JOptionPane.showMessageDialog(null,"SQL Error:"+se);
			}
			catch(Exception e){
				System.out.println(e);
				JOptionPane.showMessageDialog(null,"Error:"+e);
			}
		}
	}
}


