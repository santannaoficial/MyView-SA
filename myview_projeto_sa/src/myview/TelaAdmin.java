
package myview;
import conecta.conectar;
import user.Log_In;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import static myview.TelaCadastro.idConteudo;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Matheus Amorim da Cruz & Pedro Sant'Anna
 */
public class TelaAdmin extends javax.swing.JInternalFrame {

    /**
     * Iniciar as variáveis e carregar a classe de CorFavorito1.
     */
    public TelaAdmin() {
        initComponents();
        connection = conectar.conector();
        TblConteudos.setDefaultRenderer(Object.class, new CorFavorito());
    }
    
    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet rs = null; 
    
    public static String idConteudo = "0";
    public static String idUser = "0";

   /*
     * Método utilizado para analisar e retornar true ou false.
   */   
    private boolean Analisar() {
        PreparedStatement pstFind = null;
        ResultSet rsFind = null;
        boolean ret = false;

        String sql = "SELECT * FROM tb_content WHERE name_content = ?";
        
        try {
            pstFind = connection.prepareStatement(sql);
            pstFind.setString(1, nomeConteudo.getText());
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
    
   /*
     * Método utilizado para recarregar a tabela de conteúdos a cada informação adicionada.
   */ 
    private void TblConteudos_Recarregar(){
        if (FiltroMaiorNota.isSelected()){
            try{
                String sql = "SELECT id_user AS IdUser, name_user AS Usuario, name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito,id_content AS IdConteudo FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user ORDER BY grade_content DESC";
                pst = connection.prepareStatement(sql);
                rs = pst.executeQuery();
                TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        else{
            if (FiltroMenorNota.isSelected()){
                try{
                    String sql = "SELECT id_user AS IdUser, name_user AS Usuario, name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito,id_content AS IdConteudo FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user ORDER BY grade_content";
                    pst = connection.prepareStatement(sql);
                    rs = pst.executeQuery();
                    TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            else{
                if (FiltroFavoritos.isSelected()){
                    try{
                        String sql = "SELECT id_user AS IdUser, name_user AS Usuario, name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito,id_content AS IdConteudo FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user WHERE favorite_content = 'Sim'";
                        pst = connection.prepareStatement(sql);
                        rs = pst.executeQuery();
                        TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                else{
                    try{
                        String sql = "SELECT id_user AS IdUser, name_user AS Usuario, name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito,id_content AS IdConteudo FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user";
                        pst = connection.prepareStatement(sql);
                        rs = pst.executeQuery();
                        TblConteudos.setModel(DbUtils.resultSetToTableModel(rs));
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            }
        }
    }
    
  /*
    * Método utilizado para adicionar novas obras na tabela de conteúdo do banco de dados (tb_content).
  */ 
    private void adicionar(){
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
       String sql = "SELECT id_user AS IdUser, name_user AS Usuario, name_content AS Nome, type_content AS Tipo, grade_content AS Nota, favorite_content AS Favorito,id_content AS IdConteudo FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user WHERE name_content LIKE ?";
        try{
            pst = connection.prepareStatement(sql);
            pst.setString(1, BarraPesquisa.getText() + "%");
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
            idConteudo = TblConteudos.getModel().getValueAt(linha, 6).toString();
            idUser = TblConteudos.getModel().getValueAt(linha, 0).toString();
            Inserir_Campos();
            BarraPesquisa.setText("");
        }
        catch(Exception e){
            System.out.println(e);  
        }
    }
    
   /*
     * Método utilizado para inserir os dados armazenados na tabela e armazena nos campos respectivamentes. 
   */ 
     private void Inserir_Campos(){
        String sql = "SELECT id_content, name_content, type_content, grade_content, obs_content, date_content, tb_user_id_user, favorite_content FROM tb_content INNER JOIN tb_user ON tb_user_id_user = tb_user.id_user WHERE id_content = ? AND tb_user_id_user = ?";
        try{   
            pst = connection.prepareStatement(sql);
            pst.setString(1, idConteudo);
            pst.setString(2, idUser);
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
        boolean analisar = Analisar();
        
        if (analisar == true){
            try {
                String sql = "UPDATE tb_content SET  name_content = ?,type_content = ?,grade_content = ?,obs_content = ?,favorite_content = ? WHERE tb_user_id_user = ? AND id_content = ?";
                
                pst = connection.prepareStatement(sql);
                pst.setString(1, nomeConteudo.getText());
                pst.setString(2, tipoConteudo.getSelectedItem().toString()); 
                pst.setString(3, notaConteudo.getText());
                pst.setString(4, obsConteudo.getText());
                pst.setString(5, favoritoConteudo.getSelectedItem().toString());
                pst.setString(6, idUser);
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
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nomeConteudo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tipoConteudo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        notaConteudo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        favoritoConteudo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obsConteudo = new javax.swing.JTextArea();
        btnAdicionar = new javax.swing.JButton();
        BtnAlterar = new javax.swing.JButton();
        BtnPesquisar = new javax.swing.JButton();
        BtnDeletar = new javax.swing.JButton();
        BarraPesquisa = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblConteudos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        FiltroFavoritos = new javax.swing.JRadioButton();
        FiltroMaiorNota = new javax.swing.JRadioButton();
        FiltroMenorNota = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela de Administrador");
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

        jLabel1.setFont(new java.awt.Font("Plantagenet Cherokee", 1, 24)); // NOI18N
        jLabel1.setText("MyView");

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel2.setText("Nome da Obra:");

        nomeConteudo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomeConteudoKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel3.setText("Tipo da Obra:");

        tipoConteudo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filme", "Série", "Livro", "Jogo", "Música", "Pintura" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel4.setText("Nota (0 a 100):");

        notaConteudo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notaConteudoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                notaConteudoKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel8.setText("Adicionar aos favoritos?");

        favoritoConteudo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não ", "Sim" }));

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        jLabel5.setText("Observações:");

        obsConteudo.setColumns(20);
        obsConteudo.setRows(5);
        obsConteudo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                obsConteudoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(obsConteudo);

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add button.png"))); // NOI18N
        btnAdicionar.setMaximumSize(new java.awt.Dimension(75, 75));
        btnAdicionar.setMinimumSize(new java.awt.Dimension(75, 75));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(75, 75));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        BtnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/uptade button.png"))); // NOI18N
        BtnAlterar.setMaximumSize(new java.awt.Dimension(75, 75));
        BtnAlterar.setMinimumSize(new java.awt.Dimension(75, 75));
        BtnAlterar.setPreferredSize(new java.awt.Dimension(75, 75));
        BtnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAlterarActionPerformed(evt);
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

        BtnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete button.png"))); // NOI18N
        BtnDeletar.setMaximumSize(new java.awt.Dimension(75, 75));
        BtnDeletar.setMinimumSize(new java.awt.Dimension(75, 75));
        BtnDeletar.setPreferredSize(new java.awt.Dimension(75, 75));
        BtnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDeletarActionPerformed(evt);
            }
        });

        BarraPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BarraPesquisaKeyReleased(evt);
            }
        });

        TblConteudos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id do Usuario", "Usuário", "Nome da Obra", "Tipo", "Nota", "Favorito", "Id do Cadastro"
            }
        ));
        TblConteudos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblConteudosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TblConteudos);

        jLabel6.setText("Pesquise pelo nome do conteudo na tabela");

        jLabel7.setText("Stariote Entertainment© and Saint’Anna Solutions ©. All rights reserved.");

        FiltroFavoritos.setText("Favoritos");
        FiltroFavoritos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltroFavoritosActionPerformed(evt);
            }
        });

        FiltroMaiorNota.setText("Maior Nota");
        FiltroMaiorNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltroMaiorNotaActionPerformed(evt);
            }
        });

        FiltroMenorNota.setText("Menor Nota");
        FiltroMenorNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltroMenorNotaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel4))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(notaConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel8)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(favoritoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(nomeConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(tipoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(8, 8, 8)
                                                        .addComponent(BtnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(BtnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(BtnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(BarraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(FiltroFavoritos)
                                .addGap(18, 18, 18)
                                .addComponent(FiltroMaiorNota)
                                .addGap(18, 18, 18)
                                .addComponent(FiltroMenorNota))
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(nomeConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tipoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(notaConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(favoritoConteudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BarraPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FiltroFavoritos)
                    .addComponent(FiltroMaiorNota)
                    .addComponent(FiltroMenorNota))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nomeConteudoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeConteudoKeyTyped
       /*
         * Evento utilizado para limitar o tamanho de texto possível botar em um campo de nome.
       */   
        Character ch = evt.getKeyChar();
        if (nomeConteudo.getText().length() >= 40){
            evt.consume();
        }
    }//GEN-LAST:event_nomeConteudoKeyTyped

    private void notaConteudoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notaConteudoKeyPressed
      /*
       * Evento utilizado para bloquear o Control C e Control V no campo de nota de conteúdo.
     */    
        int ch = evt.getKeyChar();
        if (ch == 3 || ch == 22){
            evt.consume();
        }
    }//GEN-LAST:event_notaConteudoKeyPressed

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

    private void obsConteudoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obsConteudoKeyTyped
     /*
       * Evento utilizado para limitar o tamanho do texto que pode ser digitado no campo de observações.
     */  
        Character ch = evt.getKeyChar();
        if (obsConteudo.getText().length() >= 280){
            evt.consume();
        }
    }//GEN-LAST:event_obsConteudoKeyTyped

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
      /*
       * Ação utilizada para chamar método adicionar().
     */   
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void BtnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAlterarActionPerformed
      /*
       * Ação utilizada para chamar método Alterar().
     */ 
        Alterar();
    }//GEN-LAST:event_BtnAlterarActionPerformed

    private void BtnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPesquisarActionPerformed
     /*
      * Ação utilizada para pedir que o usuário informe o id do Conteúdo e chamar método Inserir_Campos().
    */     
        idConteudo = JOptionPane.showInputDialog("Id do Conteudo");
        Inserir_Campos();
    }//GEN-LAST:event_BtnPesquisarActionPerformed

    private void BtnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDeletarActionPerformed
      /*
       * Ação utilizada para chamar método Deletar().
     */    
        Deletar();
    }//GEN-LAST:event_BtnDeletarActionPerformed

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

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
     /*
       * Ação utilizada para chamar método TblConteudos_Recarregar().
    */  
        TblConteudos_Recarregar();
    }//GEN-LAST:event_formInternalFrameOpened

    private void FiltroFavoritosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltroFavoritosActionPerformed
     /*
      * Ação utilizada para chamar método TblConteudos_Recarregar().
    */  
        TblConteudos_Recarregar();
    }//GEN-LAST:event_FiltroFavoritosActionPerformed

    private void FiltroMenorNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltroMenorNotaActionPerformed
     /*
       * Ação utilizada para chamar método TblConteudos_Recarregar().
     */ 
        TblConteudos_Recarregar();
    }//GEN-LAST:event_FiltroMenorNotaActionPerformed

    private void FiltroMaiorNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltroMaiorNotaActionPerformed
    /*
     * Ação utilizada para chamar método TblConteudos_Recarregar().
    */   
        TblConteudos_Recarregar();
    }//GEN-LAST:event_FiltroMaiorNotaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BarraPesquisa;
    private javax.swing.JButton BtnAlterar;
    private javax.swing.JButton BtnDeletar;
    private javax.swing.JButton BtnPesquisar;
    private javax.swing.JRadioButton FiltroFavoritos;
    private javax.swing.JRadioButton FiltroMaiorNota;
    private javax.swing.JRadioButton FiltroMenorNota;
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
