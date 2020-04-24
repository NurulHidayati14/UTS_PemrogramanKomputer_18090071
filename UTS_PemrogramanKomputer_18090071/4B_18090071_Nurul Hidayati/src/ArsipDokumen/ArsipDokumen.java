/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArsipDokumen;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author nurul
 */
public class ArsipDokumen extends javax.swing.JFrame {
int idBaris = 0;
    String role;
    DefaultTableModel model;

    private void aturModelTabel() {
        Object[] kolom = {"NO","Kode Dokumen", "Nama Dokumen", "Kategori Dokumen", "Lokasi Dokumen", "Deskripsi Dokumen", 
            "Tanggal"};
        model = new DefaultTableModel(kolom, 0) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tblDokumen1.setModel(model);
        tblDokumen1.setRowHeight(20);
        tblDokumen1.getColumnModel().getColumn(0).setMinWidth(0);
        tblDokumen1.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    private void showForm(boolean b) {
        areaSplit1.setDividerLocation(0.3);
        areaSplit1.getLeftComponent().setVisible(b);
    }

    private void arsipdokumenForm() {
        tblDokumen1.clearSelection();
        txtKodeDok.setText("");
        txtNamaDok.setText("");
        cmbKatDok.setSelectedIndex(0);
        cmblokasi.setSelectedIndex(0);
        txtDeskripsi.setText("");
        txtTanggal.requestFocus();
    }

    private void lokasi() {
        cmblokasi.removeAllItems();
        cmblokasi.addItem("Pilih Lokasi Dokumen");
        cmblokasi.addItem("Belum ada");
        cmblokasi.addItem("Line");
        cmblokasi.addItem("Pusat");
        cmblokasi.addItem("Bisnis");
        cmblokasi.addItem("Folder");
    }

    private void KatDok() {
        cmbKatDok.removeAllItems();
        cmbKatDok.addItem("Pilih Kategori Dokumen");
        cmbKatDok.addItem("Belum ada");
        cmbKatDok.addItem("Dokumen Akte Kelahiran");
        cmbKatDok.addItem("Dokumen Kartu Keluarga");
        cmbKatDok.addItem("Dokumen Ijazah Sekolah");
        cmbKatDok.addItem("Dokumen KTP");
        cmbKatDok.addItem("Dokumen SIM");
        cmbKatDok.addItem("Dokumen Paspor");
        cmbKatDok.addItem("Dokumen Surat Tanah");
        cmbKatDok.addItem("Dokumen Surat Kepemilikan Rumah");
    }

    private void showdata(String key) {
        model.getDataVector().removeAllElements();
        String where = "";
        if (!key.isEmpty()) {
            where += "WHERE KodeDokumen LIKE '%" + key + "%'"
                    + "OR NamaDokumen LIKE '%" + key + "%' "
                    + "OR KategoriDokumen LIKE '%" + key + "%' "
                    + "OR LokasiDokumen LIKE '%" + key + "%'"
                    + "OR DeskripsiDokumen LIKE '%" + key + "%'"
                    + "OR Tanggal LIKE '%" + key + "%'";
        }
        String sql = "SELECT * FROM dokumen " + where;
        Connection con;
        Statement st;
        ResultSet rs;
        int baris = 0;
        try {
            con = Koneksi.koneksiDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object id = rs.getInt(1);
                Object KodeDok = rs.getInt(2);
                Object NamaDok = rs.getString(3);
                Object KatDok = rs.getString(4);
                Object lokasi = rs.getString(5);
                Object Deskripsi = rs.getString(6);
                Object Tanggal = rs.getString(7);
                Object[] data = {id, KodeDok, NamaDok, KatDok, lokasi, Deskripsi, Tanggal};
                model.insertRow(baris, data);
                baris++;
            }
            st.close();
            con.close();
            tblDokumen1.revalidate();
            tblDokumen1.repaint();
        } catch (SQLException e) {
            System.err.println("showData(): " + e.getMessage());
        }
    }

    private void resetView() {
        arsipdokumenForm();
        showForm(false);
        showdata("");
        btnHapus.setEnabled(false);
        idBaris = 0;
    }

    private void pilihDokumen(String n) {
        btnHapus.setEnabled(true);
        String sql = "SELECT * FROM dokumen WHERE id='" + n + "'";
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = Koneksi.koneksiDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String KodeDokumen = rs.getString(2);
                String NamaDokumen = rs.getString(3);
                Object KategoriDokumen = rs.getString(4);
                Object LokasiDokumen = rs.getString(5);
                String DeskripsiDokumen = rs.getString(6);
                String Tanggal = rs.getString(7);

                idBaris = id;

                txtNamaDok.setText(NamaDokumen);
                txtDeskripsi.setText(DeskripsiDokumen);
                txtTanggal.setText (Tanggal);

                cmbKatDok.setSelectedItem(KategoriDokumen);
                cmblokasi.setSelectedItem(LokasiDokumen);
            }
            st.close();
            con.close();
            showForm(true);
        } catch (SQLException e) {
            System.err.println("pilihDokumen(): " + e.getMessage());
        }
    }

    private void simpanDokumen() {
            String KodeDok = txtKodeDok.getText();
            String NamaDok = txtNamaDok.getText();
        int KatDok = cmbKatDok.getSelectedIndex();
        int lokasi = cmblokasi.getSelectedIndex();
        String Deskripsi = txtDeskripsi.getText();
        String Tanggal = txtTanggal.getText();
        if (KodeDok.isEmpty() || NamaDok.isEmpty() || KatDok == 0 || lokasi == 0
                || Deskripsi.isEmpty() || Tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi dokumen!");
        } else {
            String KatDok_isi = cmbKatDok.getSelectedItem().toString();
            String lokasi_isi = cmblokasi.getSelectedItem().toString();
            String sql
                   = "INSERT INTO dokumen (KodeDokumen,NamaDokumen,KategoriDokumen, "
                    + "LokasiDokumen,DeskripsiDokumen,Tanggal) "
                    + "VALUES (\"" + KodeDok + "\",\"" + NamaDok + "\","
                    + "\"" + KatDok_isi + "\",\"" + lokasi_isi + "\",\"" + Deskripsi
                    + "\",\""+ Tanggal + "\")";
            Connection con;
            Statement st; 
            try {
                con = Koneksi.koneksiDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();

                JOptionPane.showMessageDialog(this, "Dokumen telah disimpan!");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void ubahDokumen() {
        String KodeDokumen = txtKodeDok.getText();
        String NamaDokumen = txtNamaDok.getText();
        int KatDok = cmbKatDok.getSelectedIndex();
        int lokasi = cmblokasi.getSelectedIndex();
        String deskripsi = txtDeskripsi.getText();
        String tanggal = txtTanggal.getText();
        if (KodeDokumen.isEmpty() || NamaDokumen.isEmpty() || KatDok == 0 || lokasi == 0
                || deskripsi.isEmpty() || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        } else {
            String KatDok_isi = cmbKatDok.getSelectedItem().toString();
            String lokasi_isi = cmblokasi.getSelectedItem().toString();
            String sql = "UPDATE dokumen "
                    + "SET KodeDokumen=\"" + KodeDokumen + "\","
                    + "NamaDokumen=\"" + NamaDokumen + "\","
                    + "KategoriDokumen=\"" + KatDok_isi + "\","
                    + "LokasiDokumen=\"" + lokasi_isi + "\","
                    + "DeskripsiDokumen=\"" + deskripsi + "\","
                    + "Tanggal=\"" + tanggal + "\" WHERE id=\"" + idBaris + "\"";
            Connection con;
            Statement st;
            try {
                con = Koneksi.koneksiDB();
                st = con.createStatement();
                st.executeUpdate(sql);

                st.close();
                con.close();
                resetView();

                JOptionPane.showMessageDialog(this, "Dokumen telah ditambah!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void hapusDokumen(int baris) {
        Connection con;
        Statement st;
        try {
            con = Koneksi.koneksiDB();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM dokumen WHERE id=" + baris);
            st.close();
            con.close();
            resetView();
            JOptionPane.showMessageDialog(this, "Dokumen telah dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    public ArsipDokumen() {
        initComponents();
        aturModelTabel();
        lokasi();
        KatDok();
        showForm(false);
        showdata("");
        Koneksi.koneksiDB();
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnCari = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDokumen1 = new javax.swing.JTable();
        areaSplit1 = new javax.swing.JSplitPane();
        panelKiri1 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        txtNamaDok = new javax.swing.JTextField();
        cmbKatDok = new javax.swing.JComboBox<String>();
        cmblokasi = new javax.swing.JComboBox<String>();
        txtDeskripsi = new javax.swing.JTextField();
        btnTutup1 = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jTextField11 = new javax.swing.JTextField();
        txtTanggal = new javax.swing.JTextField();
        txtKodeDok = new javax.swing.JTextField();
        txtCari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 255, 204));

        btnTambah.setText("Tambah Data");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus Data");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnLogout.setText("LogOut");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        tblDokumen1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblDokumen1);

        areaSplit1.setDividerLocation(300);
        areaSplit1.setOneTouchExpandable(true);

        panelKiri1.setBackground(new java.awt.Color(0, 204, 255));

        jTextField6.setText("Kode Dokumen :");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setText("Nama Dokumen :");

        jTextField8.setText("Kategori Dokumen :");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jTextField9.setText("Lokasi Dokumen :");

        jTextField10.setText("Deskripsi Dokumen :");

        cmbKatDok.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmblokasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTutup1.setText("Tutup");
        btnTutup1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutup1ActionPerformed(evt);
            }
        });

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jTextField11.setText("Tanggal : ");

        txtTanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalActionPerformed(evt);
            }
        });

        txtKodeDok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeDokActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKiri1Layout = new javax.swing.GroupLayout(panelKiri1);
        panelKiri1.setLayout(panelKiri1Layout);
        panelKiri1Layout.setHorizontalGroup(
            panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(panelKiri1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiri1Layout.createSequentialGroup()
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTanggal))
                    .addGroup(panelKiri1Layout.createSequentialGroup()
                        .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKiri1Layout.createSequentialGroup()
                                .addComponent(btnTutup1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKiri1Layout.createSequentialGroup()
                                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelKiri1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtNamaDok, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbKatDok, javax.swing.GroupLayout.Alignment.LEADING, 0, 158, Short.MAX_VALUE)
                                            .addComponent(txtKodeDok, javax.swing.GroupLayout.Alignment.LEADING)))
                                    .addGroup(panelKiri1Layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelKiri1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtDeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKiri1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmblokasi, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        panelKiri1Layout.setVerticalGroup(
            panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiri1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKodeDok, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDok, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKatDok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmblokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(txtTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(panelKiri1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnTutup1))
                .addContainerGap())
        );

        areaSplit1.setLeftComponent(panelKiri1);

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus))
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(areaSplit1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(btnHapus))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogout))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCari)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaSplit1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        role = "Tambah";
        btnSimpan.setText("Simpan");
        idBaris = 0;
        arsipdokumenForm();
        showForm(true);
        btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (idBaris == 1 ) {
            JOptionPane.showMessageDialog(this, "Pilih dokumen yang akan "
                + "dihapus!");
        }else{
            hapusDokumen(idBaris);
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void btnTutup1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutup1ActionPerformed
        arsipdokumenForm();
        showForm(false);
        btnHapus.setEnabled(false);
        idBaris = 0;
    }//GEN-LAST:event_btnTutup1ActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (role.equals("Tambah")) {
            simpanDokumen();
        }else if (role.equals("Ubah")) {
            ubahDokumen();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        String key = txtCari.getText();
        showdata(key);
    }//GEN-LAST:event_txtCariKeyReleased
private void formComponentResized(java.awt.event.ComponentEvent evt) {                                      
        role = "Ubah";
        int row = tblDokumen1.getRowCount();
        if (row >0) {
            int sel = tblDokumen1.getSelectedRow();
            if(sel !=-1){
                pilihDokumen(tblDokumen1.getValueAt(sel, 0).toString());
                btnSimpan.setText("UBAH DATA");
            }
        }
    }
    private void txtTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTanggalActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        Login dp = new Login();
        this.setVisible(false);
        dp.setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void txtKodeDokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeDokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeDokActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ArsipDokumen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ArsipDokumen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ArsipDokumen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ArsipDokumen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ArsipDokumen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit1;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup1;
    private javax.swing.JComboBox<String> cmbKatDok;
    private javax.swing.JComboBox<String> cmblokasi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel panelKiri1;
    private javax.swing.JTable tblDokumen1;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtDeskripsi;
    private javax.swing.JTextField txtKodeDok;
    private javax.swing.JTextField txtNamaDok;
    private javax.swing.JTextField txtTanggal;
    // End of variables declaration//GEN-END:variables
}
