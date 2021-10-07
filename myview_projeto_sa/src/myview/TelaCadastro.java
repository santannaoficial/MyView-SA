package myview;

import conecta.conectar;
import user.Log_In;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Matheus Amorim da Cruz & Pedro Sant'Anna
 */
public class TelaCadastro extends javax.swing.JInternalFrame {
        
    /**
     * Iniciar as variáveis e carregar a classe de CorFavorito2.
     */
    public TelaCadastro() {
        initComponents();
        connection = conectar.conector();
        TblConteudos.setDefaultRenderer(Object.class, new CorFavorito2());
    } 
    
    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet rs = null; 
    
    public static String idConteudo = "0";
    
   /*
     * Método utilizado para recarregar a tabela de conteúdos a cada informação adicionada.
   */ 
    private void TblConteudos_Recarregar(){
        if (FiltroFavorito.isSelected()){
            try{
                String sql = "SELECT name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito, id_content AS Id FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user WHERE tb_user_id_user = ? AND favorite_content = 'Sim'";
                pst = connection.prepareStatement(sql);
                pst.setString(1, Log_In.idUser);
                rs = pst.executeQuery();
                TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        else{
            try{
                String sql = "SELECT name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito, id_content AS Id FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user WHERE tb_user_id_user = ?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, Log_In.idUser);
                rs = pst.executeQuery();
                TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
   /*
    * Método utilizado para adicionar novas obras na tabela de conteúdo do banco de dados (tb_content).
   */ 
    private void Adicionar(){
        int x = 0;
        String sql = "insert into tb_content(name_content,type_content,grade_content,obs_content,favorite_content,tb_user_id_user) values(?,?,?,?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, nomeConteudo.getText());
            pst.setString(2, tipoConteudo.getSelectedItem().toString()); 
            pst.setString(3, notaConteudo.getText());
            pst.setString(4, obsConteudo.getText());
            pst.setString(5, favoritoConteudo.getSelectedItem().toString());
            pst.setString(6, Log_In.idUser);
            
            
            if (nomeConteudo.getText().isEmpty() || notaConteudo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o nome da obra e a sua nota.");
                nomeConteudo.grabFocus();
            }
            else{
                 x = Integer.parseInt(notaConteudo.getText());
                 if(x > 100){
                 JOptionPane.showMessageDialog(null, "A nota máxima é 100.");
                }
                else{    
                     boolean Foundya= Analisar();
                     System.out.println(Foundya);

                     if (Foundya== true) {
                        JOptionPane.showMessageDialog(null, "Obra já cadastrada.");
                         nomeConteudo.grabFocus();
                      }
                      else{
                       int adicionado = pst.executeUpdate();
                       TblConteudos_Recarregar();
                    
                       if (adicionado > 0){
                          JOptionPane.showMessageDialog(null, "Obra cadastrada com sucesso.");
                          nomeConteudo.setText(null);
                          notaConteudo.setText(null);
                          obsConteudo.setText(null);
                          nomeConteudo.grabFocus();
                       }
                    }
                }
            }
        }
        catch(Exception e){ e.printStackTrace(); }{
            System.out.println("e");
        }
    }
    
    /*
     * Método utilizado para pesquisar o nome de uma Obra através da barra de pesquisa.
    */   
    private void BarraPesquisa(){
        String sql = "SELECT name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito,id_content AS Id FROM tb_content INNER JOIN tb_user ON tb_user_id_user = id_user WHERE name_content LIKE ? AND tb_user_id_user = ?";        
        try{
            pst = connection.prepareStatement(sql);
            pst.setString(1, BarraPesquisa.getText() + "%");
            pst.setString(2, Log_In.idUser);
            rs = pst.executeQuery();
            TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    
   /*
     * Método utilizado Selecionar um campo da tabela de conteúdos e chamar o método Inserir_Campos().
   */   
    private void Selecionar_Campo() {
        try{
            int linha = TblConteudos.getSelectedRow();
            
            idConteudo = TblConteudos.getModel().getValueAt(linha, 4).toString();
            Inserir_Campos();
        }
        catch(Exception e){
            System.out.println(e);  
        }
    }
  
   /*
    * Método utilizado para inserir os dados armazenados na tabela e armazena nos campos respectivamentes. 
   */ 
    private void Inserir_Campos(){
        String sql = "SELECT id_content, name_content, type_content, grade_content, obs_content, date_content, tb_user_id_user, favorite_content FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user WHERE id_content = ? AND tb_user_id_user = ?;";
        try{   
            pst = connection.prepareStatement(sql);
            pst.setString(1, idConteudo);
            pst.setString(2, Log_In.idUser);
            rs = pst.executeQuery();
            if (rs.next()) {
                String idConteudoAux = rs.getString(1);
                nomeConteudo.setText(rs.getString(2));
                tipoConteudo.setSelectedItem(rs.getString(3));
                notaConteudo.setText(rs.getString(4));
                obsConteudo.setText(rs.getString(5));
                String dataConteudo = rs.getString(6);
                String idUserAux = rs.getString(7);
                favoritoConteudo.setSelectedItem(rs.getString(8));
            }
            else{
                JOptionPane.showMessageDialog(null, "Conteudo não cadastrado ou informado.");
            }
        }
        catch(Exception e){
            System.out.println(e); 
        }
    }
    
   /*
    * Método utilizado para alterar informações armazenadas na tabela de conteúdo (tb_content) através da busca do id do conteúdo.
   */  
    private void Alterar(){
        boolean analizar = Analisar();
        
        if (analizar == true){
            try {
                String sql = "UPDATE tb_content SET  name_content = ?,type_content = ?,grade_content = ?,obs_content = ?,favorite_content = ? WHERE tb_user_id_user = ? AND id_content = ?";

                pst = connection.prepareStatement(sql);

                pst.setString(1, nomeConteudo.getText());
                pst.setString(2, tipoConteudo.getSelectedItem().toString()); 
                pst.setString(3, notaConteudo.getText());
                pst.setString(4, obsConteudo.getText());
                pst.setString(5, favoritoConteudo.getSelectedItem().toString());
                pst.setString(6, Log_In.idUser);
                pst.setString(7, idConteudo);

                if (nomeConteudo.getText().isEmpty() || notaConteudo.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha os campos de nome e nota para realizar alteração.");
                    nomeConteudo.grabFocus();
                }
                else{
                    boolean Foundya= Analisar();
                    System.out.println(Foundya);

                    try{
                        int adicionado = pst.executeUpdate();
                        TblConteudos_Recarregar();
                        
                        if (adicionado > 0){
                        JOptionPane.showMessageDialog(null, "Obra alterada com sucesso.");
                        nomeConteudo.setText(null);
                        notaConteudo.setText(null);
                        obsConteudo.setText(null);
                        nomeConteudo.grabFocus();
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
            catch(Exception e2){ e2.printStackTrace(); }{
                System.out.println("e");
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Conteudo não cadastrado. Não há alteração.");
            nomeConteudo.grabFocus();
        }
    }
    
   /*
    * Método utilizado para deletar informações armazenadas na tabela de conteúdo (tb_content) através da busca do id do conteúdo.
   */   
    private void Deletar() {  
        String sql = "DELETE FROM tb_content WHERE id_content = ?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, idConteudo);

            int excluir = JOptionPane.showConfirmDialog(null, "Confirma a exclusão?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (excluir == JOptionPane.YES_OPTION) {
                int excluido = pst.executeUpdate();
                if (excluido > 0) {
                    TblConteudos_Recarregar();
                    JOptionPane.showMessageDialog(null, "Exclusão realizada com sucesso");
                    nomeConteudo.setText(null);
                    notaConteudo.setText(null);
                    obsConteudo.setText(null);
                    nomeConteudo.grabFocus();
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }    
    }
    
    /*
     * Método utilizado para analisar e retornar true ou false.
    */ 
    private boolean Analisar() {
        PreparedStatement pstFind = null;
        ResultSet rsFind = null;
        boolean ret = false;

        String sql = "SELECT * FROM tb_content WHERE name_content =? AND tb_user_id_user=?";
        
        try {
            pstFind = connection.prepareStatement(sql);
            pstFind.setString(1, nomeConteudo.getText());
            pstFind.setString(2, Log_In.idUser);
            rsFind = pstFind.executeQuery();
            if (rsFind.next()) {
                ret = true;
            }
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return ret;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnAdicionar = new javax.swing.JButton();
        BtnPesquisar = new javax.swing.JButton();
        nomeConteudo = new javax.swing.JTextField();
        notaConteudo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        obsConteudo = new javax.swing.JTextArea();
        tipoConteudo = new javax.swing.JComboBox();
        favoritoConteudo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        BtnAlterar = new javax.swing.JButton();
        BtnDeletar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblConteudos = new javax.swing.JTable();
        BarraPesquisa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        FiltroFavorito = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("MyView");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel2.setText("Nome da Obra:");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel3.setText("Tipo da Obra:");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel4.setText("Nota (0 a 100):");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel5.setText("Observações:");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel8.setText("Adicionar aos favoritos?");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add button.png"))); // NOI18N
        btnAdicionar.setMaximumSize(new java.awt.Dimension(75, 75));
        btnAdicionar.setMinimumSize(new java.awt.Dimension(75, 75));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(75, 75));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        BtnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search button.png"))); // NOI18N
        BtnPesquisar.setMaximumSize(new java.awt.Dimension(75, 75));
        BtnPesquisar.setMinimumSize(new java.awt.Dimension(75, 75));
        BtnPesquisar.setPreferredSize(new java.awt.Dimension(75, 75));
        BtnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPesquisarActionPerformed(evt);
            }
        });

        nomeConteudo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomeConteudoKeyTyped(evt);
            }
        });

        notaConteudo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notaConteudoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                notaConteudoKeyTyped(evt);
            }
        });

        obsConteudo.setColumns(20);
        obsConteudo.setRows(5);
        obsConteudo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                obsConteudoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(obsConteudo);

        tipoConteudo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filme", "Série", "Livro", "Jogo", "Música", "Pintura" }));

        favoritoConteudo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não ", "Sim" }));

        jLabel6.setText("Stariote Entertainment© and Saint’Anna Solutions ©. All rights reserved.");

        BtnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/uptade button.png"))); // NOI18N
        BtnAlterar.setMaximumSize(new java.awt.Dimension(75, 75));
        BtnAlterar.setMinimumSize(new java.awt.Dimension(75, 75));
        BtnAlterar.setPreferredSize(new java.awt.Dimension(75, 75));
        BtnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAlterarActionPerformed(evt);
            }
        });

        BtnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete button.png"))); // NOI18N
        BtnDeletar.setMaximumSize(new java.awt.Dimension(75, 75));
        BtnDeletar.setMinimumSize(new java.awt.Dimension(75, 75));
        BtnDeletar.setPreferredSize(new java.awt.Dimension(75, 75));
        BtnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDeletarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Plantagenet Cherokee", 1, 24)); // NOI18N
        jLabel1.setText("MyView");

        TblConteudos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nome", "Tipo", "Nota", "Favorito", "Id"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblConteudos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblConteudosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TblConteudos);

        BarraPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BarraPesquisaKeyReleased(evt);
            }
        });

        jLabel7.setText("Pesquisar na biblioteca");

        FiltroFavorito.setText("Filtrar por favoritos");
        FiltroFavorito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltroFavoritoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(BtnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(BtnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(BtnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel5)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(tipoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(notaConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(favoritoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(nomeConteudo)))
                        .addComponent(jScrollPane1))
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BarraPesquisa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FiltroFavorito)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(nomeConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(tipoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(notaConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(favoritoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(BtnPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BtnAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BarraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(FiltroFavorito))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getAccessibleContext().setAccessibleName("myview");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void obsConteudoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obsConteudoKeyTyped
      /*
       * Evento utilizado para limitar o tamanho do texto que pode ser digitado no campo de observações.
      */  
        Character ch = evt.getKeyChar();
        if (obsConteudo.getText().length() >= 280){
            evt.consume();
        }
    }//GEN-LAST:event_obsConteudoKeyTyped

    private void notaConteudoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notaConteudoKeyTyped
       /*
         * Evento utilizado para limitar o tamanho do texto que pode ser digitado no campo de nota e limitar o teclado a escrever apenas números.
       */  
        Character ch = evt.getKeyChar();
        if (notaConteudo.getText().length() >= 3){
            evt.consume();
        }

        String caracteres="0987654321";
        if(!caracteres.contains(evt.getKeyChar()+"")){
            evt.consume();
        }
    }//GEN-LAST:event_notaConteudoKeyTyped

    private void nomeConteudoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeConteudoKeyTyped
       /*
         * Evento utilizado para limitar o tamanho de texto possível botar em um campo de nome.
       */   
        Character ch = evt.getKeyChar();
        if (nomeConteudo.getText().length() >= 40){
            evt.consume();
        }
    }//GEN-LAST:event_nomeConteudoKeyTyped

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
       /*
        * Ação utilizada para chamar método adicionar().
       */  
        Adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void BtnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAlterarActionPerformed
       /*
        * Ação utilizada para chamar método Alterar().
       */  
        Alterar();
    }//GEN-LAST:event_BtnAlterarActionPerformed

    private void BtnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDeletarActionPerformed
        /*
         * Ação utilizada para chamar método Deletar().
        */   
        Deletar();
    }//GEN-LAST:event_BtnDeletarActionPerformed

    private void notaConteudoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notaConteudoKeyPressed
       /*
        * Evento utilizado para bloquear o Control C e Control V no campo de nota de conteúdo.
       */      
        int ch = evt.getKeyChar(); 
        if (ch == 3 || ch == 22){ 
        evt.consume();
        }
    }//GEN-LAST:event_notaConteudoKeyPressed

    private void BarraPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BarraPesquisaKeyReleased
       /*
        * Ação utilizada para chamar método BarraPesquisa().
       */ 
        BarraPesquisa();
    }//GEN-LAST:event_BarraPesquisaKeyReleased

    private void TblConteudosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblConteudosMouseClicked
       /*
        * Ação utilizada para chamar método Selecionar_Campo().
       */ 
        Selecionar_Campo();
    }//GEN-LAST:event_TblConteudosMouseClicked

    private void BtnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPesquisarActionPerformed
        /*
         * Ação utilizada para chamar método BarraPesquisa().
       */
        idConteudo = JOptionPane.showInputDialog("Id do Conteudo");
        Inserir_Campos();
    }//GEN-LAST:event_BtnPesquisarActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
       /*
        * Ação utilizada para chamar método TblConteudos_Recarregar().
      */ 
        TblConteudos_Recarregar();
    }//GEN-LAST:event_formInternalFrameOpened

    private void FiltroFavoritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltroFavoritoActionPerformed
      /*
       * Ação utilizada para chamar método TblConteudos_Recarregar().
     */ 
        TblConteudos_Recarregar();
    }//GEN-LAST:event_FiltroFavoritoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BarraPesquisa;
    private javax.swing.JButton BtnAlterar;
    private javax.swing.JButton BtnDeletar;
    private javax.swing.JButton BtnPesquisar;
    private javax.swing.JRadioButton FiltroFavorito;
    private javax.swing.JTable TblConteudos;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JComboBox favoritoConteudo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nomeConteudo;
    private javax.swing.JTextField notaConteudo;
    private javax.swing.JTextArea obsConteudo;
    private javax.swing.JComboBox tipoConteudo;
    // End of variables declaration//GEN-END:variables
}
