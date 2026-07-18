package tubes.view.admin;

import tubes.controller.KategoriController;
import tubes.controller.MenuController;
import tubes.model.Kategori;
import tubes.model.Menu;
import tubes.util.AppColors;
import tubes.util.CurrencyUtil;
import tubes.util.ImageUtil;
import tubes.view.components.RoundedButton;
import tubes.view.components.VirtualKeyboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MenuManageView extends JPanel {

    private final AdminDashboard parent;
    private final MenuController menuCtrl;
    private final KategoriController kategoriCtrl;

    private DefaultTableModel tableModel;
    private JTable menuTable;

    private JTextField txtNama;
    private JComboBox<Kategori> cbKategori;
    private JTextField txtHarga;
    private JTextField txtStok;
    private JLabel lblGambar;
    private JLabel imgPreview;
    private File selectedImageFile;
    private int editingId = -1;
    private boolean isEditing = false;

    public MenuManageView(AdminDashboard parent) {
        this.parent = parent;
        this.menuCtrl = new MenuController();
        this.kategoriCtrl = new KategoriController();
        setLayout(new BorderLayout());
        setBackground(AppColors.MC_LIGHT_GRAY);
        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Kelola Menu");
        title.setFont(AppColors.FONT_TITLE);
        title.setForeground(AppColors.MC_DARK);
        RoundedButton btnRefresh = new RoundedButton("Refresh", AppColors.MC_DARK, Color.WHITE);
        btnRefresh.setPreferredSize(new Dimension(110, 38));
        btnRefresh.addActionListener(e -> loadData());
        header.add(title, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildTablePanel(), buildFormPanel());
        split.setDividerLocation(620);
        split.setDividerSize(4);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 8));

        JTextField txtSearch = new JTextField();
        txtSearch.setFont(AppColors.FONT_BODY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                new EmptyBorder(6, 10, 6, 10)));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(txtSearch.getText().trim());
            }
        });
        VirtualKeyboard.attach(txtSearch);

        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setOpaque(false);
        searchBar.setBorder(new EmptyBorder(0, 0, 12, 0));
        searchBar.add(new JLabel("Cari: "), BorderLayout.WEST);
        searchBar.add(txtSearch, BorderLayout.CENTER);

        String[] cols = {"ID", "Nama Menu", "Kategori", "Harga", "Stok"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        menuTable = new JTable(tableModel);
        menuTable.setFont(AppColors.FONT_BODY);
        menuTable.setRowHeight(44);
        menuTable.setShowGrid(false);
        menuTable.setSelectionBackground(new Color(0xFF, 0xF0, 0xCC));
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        menuTable.getTableHeader().setBackground(AppColors.MC_RED);
        menuTable.getTableHeader().setForeground(Color.WHITE);
        menuTable.getTableHeader().setReorderingAllowed(false);

        int[] widths = {40, 210, 110, 110, 60};
        for (int i = 0; i < widths.length; i++) {
            menuTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        menuTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = menuTable.getSelectedRow();
                if (row >= 0) {
                    loadToForm(row);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(menuTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnRow.setOpaque(false);
        RoundedButton btnNew = new RoundedButton("+ Tambah Baru", AppColors.MC_RED, Color.WHITE);
        RoundedButton btnDel = new RoundedButton("Hapus", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnNew.setPreferredSize(new Dimension(140, 38));
        btnDel.setPreferredSize(new Dimension(100, 38));
        btnNew.addActionListener(e -> resetForm());
        btnDel.addActionListener(e -> deleteSelected());
        btnRow.add(btnNew);
        btnRow.add(btnDel);

        panel.add(searchBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 8, 16, 16));

        JLabel formTitle = new JLabel("Form Menu");
        formTitle.setFont(AppColors.FONT_HEADING);
        formTitle.setForeground(AppColors.MC_DARK);
        formTitle.setBorder(new EmptyBorder(0, 0, 16, 0));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1;
        int row = 0;

        gbc.gridy = row++;
        fields.add(fLbl("Nama Menu *"), gbc);
        txtNama = new JTextField();
        styleF(txtNama);
        VirtualKeyboard.attach(txtNama);
        gbc.gridy = row++;
        fields.add(txtNama, gbc);

        gbc.gridy = row++;
        fields.add(fLbl("Kategori *"), gbc);
        cbKategori = new JComboBox<>();
        refreshKategoriCombo();
        cbKategori.setFont(AppColors.FONT_BODY);
        cbKategori.setPreferredSize(new Dimension(0, 44));
        gbc.gridy = row++;
        fields.add(cbKategori, gbc);

        gbc.gridy = row++;
        fields.add(fLbl("Harga (Rp) *"), gbc);
        txtHarga = new JTextField();
        styleF(txtHarga);
        VirtualKeyboard.attach(txtHarga);
        gbc.gridy = row++;
        fields.add(txtHarga, gbc);

        gbc.gridy = row++;
        fields.add(fLbl("Stok *"), gbc);
        txtStok = new JTextField();
        styleF(txtStok);
        VirtualKeyboard.attach(txtStok);
        gbc.gridy = row++;
        fields.add(txtStok, gbc);

        gbc.gridy = row++;
        fields.add(fLbl("Gambar"), gbc);
        lblGambar = new JLabel("Belum ada gambar");
        lblGambar.setFont(AppColors.FONT_SMALL);
        lblGambar.setForeground(AppColors.MC_GRAY);
        gbc.gridy = row++;
        fields.add(lblGambar, gbc);

        imgPreview = new JLabel();
        imgPreview.setPreferredSize(new Dimension(120, 90));
        imgPreview.setBorder(BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1));
        imgPreview.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        fields.add(imgPreview, gbc);

        RoundedButton btnGambar = new RoundedButton("Pilih Gambar", new Color(0x60, 0x60, 0x60), Color.WHITE);
        btnGambar.setPreferredSize(new Dimension(160, 40));
        btnGambar.addActionListener(e -> chooseImage());
        gbc.gridy = row++;
        fields.add(btnGambar, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        RoundedButton btnSave = new RoundedButton("Simpan", AppColors.MC_RED, Color.WHITE);
        RoundedButton btnReset = new RoundedButton("Reset", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnReset.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.addActionListener(e -> saveMenu());
        btnReset.addActionListener(e -> resetForm());
        btnPanel.add(btnSave);
        btnPanel.add(btnReset);
        gbc.gridy = row;
        gbc.insets = new Insets(16, 0, 0, 0);
        fields.add(btnPanel, gbc);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.add(formTitle, BorderLayout.NORTH);
        inner.add(fields, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JLabel fLbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(AppColors.MC_DARK);
        return l;
    }

    private void styleF(JTextField f) {
        f.setFont(AppColors.FONT_BODY);
        f.setPreferredSize(new Dimension(0, 44));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                new EmptyBorder(6, 12, 6, 12)));
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppColors.MC_YELLOW, 2),
                        new EmptyBorder(6, 12, 6, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                        new EmptyBorder(6, 12, 6, 12)));
            }
        });
    }

    public void refreshKategoriCombo() {
        Kategori selected = (Kategori) cbKategori.getSelectedItem();
        cbKategori.removeAllItems();
        for (Kategori k : kategoriCtrl.getAllKategori()) {
            cbKategori.addItem(k);
        }
        if (selected != null) {
            cbKategori.setSelectedItem(selected);
        }
    }

    private void loadData() {
        SwingWorker<List<Menu>, Void> w = new SwingWorker<>() {
            @Override
            protected List<Menu> doInBackground() {
                return menuCtrl.getAllMenu();
            }

            @Override
            protected void done() {
                try {
                    populate(get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        w.execute();
    }

    private void populate(List<Menu> list) {
        tableModel.setRowCount(0);
        for (Menu m : list) {
            tableModel.addRow(new Object[]{
                m.getId(), m.getNamaMeu(),
                m.getKategori() != null ? m.getKategori().getNama() : "-",
                CurrencyUtil.formatShort(m.getHarga()),
                m.getStok()
            });
        }
    }

    private void filterTable(String kw) {
        SwingWorker<List<Menu>, Void> w = new SwingWorker<>() {
            @Override
            protected List<Menu> doInBackground() {
                return menuCtrl.searchMenu(kw);
            }

            @Override
            protected void done() {
                try {
                    populate(get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        w.execute();
    }

    private void loadToForm(int row) {
        Object idObj = tableModel.getValueAt(row, 0);
        if (idObj == null) {
            return;
        }
        Menu menu = menuCtrl.getMenuById((int) idObj);
        if (menu == null) {
            return;
        }
        isEditing = true;
        editingId = menu.getId();
        txtNama.setText(menu.getNamaMeu());
        cbKategori.setSelectedItem(menu.getKategori());
        txtHarga.setText(String.valueOf(menu.getHarga()));
        txtStok.setText(String.valueOf(menu.getStok()));
        if (menu.getGambar() != null && !menu.getGambar().isEmpty()) {
            lblGambar.setText(menu.getGambar());
            imgPreview.setIcon(ImageUtil.loadMenuImage(menu.getGambar(), 120, 90));
        } else {
            lblGambar.setText("Tidak ada gambar");
            imgPreview.setIcon(null);
        }
        selectedImageFile = null;
    }

    private void resetForm() {
        isEditing = false;
        editingId = -1;
        selectedImageFile = null;
        txtNama.setText("");
        if (cbKategori.getItemCount() > 0) {
            cbKategori.setSelectedIndex(0);
        }
        txtHarga.setText("");
        txtStok.setText("");
        lblGambar.setText("Belum ada gambar");
        imgPreview.setIcon(null);
        menuTable.clearSelection();
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "png", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = chooser.getSelectedFile();
            lblGambar.setText(selectedImageFile.getName());
            Image img = new ImageIcon(selectedImageFile.getAbsolutePath())
                    .getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
            imgPreview.setIcon(new ImageIcon(img));
        }
    }

    private void saveMenu() {
        if (cbKategori.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada kategori. Tambahkan kategori dahulu di tab Kelola Kategori.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Kategori kategori = (Kategori) cbKategori.getSelectedItem();
        String err = isEditing
                ? menuCtrl.updateMenu(editingId, txtNama.getText(), kategori,
                        txtHarga.getText(), txtStok.getText(), selectedImageFile)
                : menuCtrl.tambahMenu(txtNama.getText(), kategori,
                        txtHarga.getText(), txtStok.getText(), selectedImageFile);

        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Validasi", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    isEditing ? "Menu berhasil diupdate!" : "Menu berhasil ditambahkan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            resetForm();
        }
    }

    private void deleteSelected() {
        int row = menuTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih menu dahulu.");
            return;
        }
        String nama = tableModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus \"" + nama + "\"?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        String err = menuCtrl.hapusMenu((int) tableModel.getValueAt(row, 0));
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Menu dihapus.");
            loadData();
            resetForm();
        }
    }
}
