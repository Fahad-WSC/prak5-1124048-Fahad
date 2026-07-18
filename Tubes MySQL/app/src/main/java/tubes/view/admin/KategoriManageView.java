package tubes.view.admin;

import tubes.controller.KategoriController;
import tubes.controller.MenuController;
import tubes.model.Kategori;
import tubes.util.AppColors;
import tubes.view.components.RoundedButton;
import tubes.view.components.VirtualKeyboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class KategoriManageView extends JPanel {

    private final AdminDashboard parent;
    private final KategoriController kategoriCtrl;
    private final MenuController menuCtrl;

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtNama;
    private int editingId = -1;

    public KategoriManageView(AdminDashboard parent) {
        this.parent = parent;
        this.kategoriCtrl = new KategoriController();
        this.menuCtrl = new MenuController();
        setLayout(new BorderLayout());
        setBackground(AppColors.MC_LIGHT_GRAY);
        buildUI();
        loadData();
    }

    private void buildUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Kelola Kategori");
        title.setFont(AppColors.FONT_TITLE);
        title.setForeground(AppColors.MC_DARK);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildTablePanel(), buildFormPanel());
        split.setDividerLocation(500);
        split.setDividerSize(4);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 8));

        String[] cols = {"ID", "Nama Kategori"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(AppColors.FONT_BODY);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(0xFF, 0xF0, 0xCC));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.MC_RED);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    editingId = (int) tableModel.getValueAt(row, 0);
                    txtNama.setText((String) tableModel.getValueAt(row, 1));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xEE, 0xEE, 0xEE), 1));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnRow.setOpaque(false);
        RoundedButton btnNew = new RoundedButton("+ Kategori Baru", AppColors.MC_RED, Color.WHITE);
        RoundedButton btnDel = new RoundedButton("Hapus", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnNew.setPreferredSize(new Dimension(150, 38));
        btnDel.setPreferredSize(new Dimension(100, 38));
        btnNew.addActionListener(e -> resetForm());
        btnDel.addActionListener(e -> deleteSelected());
        btnRow.add(btnNew);
        btnRow.add(btnDel);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 8, 16, 16));

        JLabel formTitle = new JLabel("Form Kategori");
        formTitle.setFont(AppColors.FONT_HEADING);
        formTitle.setForeground(AppColors.MC_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(formTitle);

        JLabel lbl = new JLabel("Nama Kategori *");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));

        txtNama = new JTextField();
        txtNama.setFont(AppColors.FONT_BODY);
        txtNama.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNama.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                new EmptyBorder(8, 12, 8, 12)));
        txtNama.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtNama.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppColors.MC_YELLOW, 2),
                        new EmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtNama.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xDD, 0xDD, 0xDD), 1),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });
        VirtualKeyboard.attach(txtNama);
        panel.add(txtNama);
        panel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        RoundedButton btnSave = new RoundedButton("Simpan", AppColors.MC_RED, Color.WHITE);
        RoundedButton btnReset = new RoundedButton("Reset", AppColors.MC_GRAY, AppColors.MC_DARK);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnReset.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnSave.addActionListener(e -> saveKategori());
        btnReset.addActionListener(e -> resetForm());
        btnPanel.add(btnSave);
        btnPanel.add(btnReset);
        panel.add(btnPanel);

        return panel;
    }

    private void loadData() {
        List<Kategori> list = kategoriCtrl.getAllKategori();
        tableModel.setRowCount(0);
        for (Kategori k : list) {
            tableModel.addRow(new Object[]{k.getId(), k.getNama()});
        }
    }

    private void resetForm() {
        editingId = -1;
        txtNama.setText("");
        table.clearSelection();
    }

    private void saveKategori() {
        String nama = txtNama.getText().trim();
        String err = (editingId > 0)
                ? kategoriCtrl.updateKategori(editingId, nama)
                : kategoriCtrl.tambahKategori(nama);

        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                editingId > 0 ? "Kategori diperbarui." : "Kategori ditambahkan.",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        loadData();
        resetForm();
        parent.refreshMenuKategoriCombo();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih kategori dahulu.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String nama = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus kategori \"" + nama + "\"?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        String err = kategoriCtrl.hapusKategori(id, menuCtrl);
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Kategori dihapus.");
            loadData();
            resetForm();
            parent.refreshMenuKategoriCombo();
        }
    }
}
