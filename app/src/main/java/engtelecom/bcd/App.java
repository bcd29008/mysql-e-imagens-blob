package engtelecom.bcd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import engtelecom.bcd.db.ConnectionFactory;

/**
 * Um pequeno exemplo para ilustrar o processo para inserir e recuperar uma imagem (arquivo binário) no banco de dados MySQL. No MySQL é necessário ter uma coluna do tipo BLOB (Binary Large OBjec).
 * 
 * Veja o arquivo tabela.sql na raiz deste repositório.
 * 
 */
public class App {
   

    /**
     * Para cadastrar uma nova cidade no banco de dados
     * @param nome Nome da cidade
     * @param pais País onde a cidade está situada
     * @param arquivo Arquivo binário contendo a foto da cidade
     * @return total de linhas afetadas com a operação de INSERT
     * @throws IOException
     */
    public int cadastrarCidade(String nome, String pais, File arquivo) throws IOException{

        if ((arquivo != null)&& (nome !=null) && (pais != null)){
            String query = "INSERT INTO Cidade (nome, pais, foto) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = ConnectionFactory.getDBConnection().prepareStatement(query)){

                // para ler arquivo do disco
                FileInputStream inputStream = new FileInputStream(arquivo);

                // definindo os parâmetros do PreparedStatement
                stmt.setString(1, nome);
                stmt.setString(2, pais);
                stmt.setBinaryStream(3, inputStream);

                // Lendo o arquivo do disco
                System.out.println("Lendo arquivo: " + arquivo.getAbsolutePath());
                System.out.println("Incluindo linha no banco de dados...");
                return stmt.executeUpdate();
            }catch(SQLException e) {
            System.err.println("Erro ao inserir no banco de dados: " + e.toString());   
            }
        }
        return 0;
    }


    /**
     * Irá obter a foto da cidade do banco de dados e fará sua exibição em uma janela gráfica
     * @param idCidade
     */
    public void exibirFotoDaCidade(int idCidade){
        String query = "SELECT * FROM Cidade WHERE idCidade = ?";

        try (PreparedStatement stmt = ConnectionFactory.getDBConnection().prepareStatement(query)){

            stmt.setInt(1, idCidade);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                // Instanciando uma imagem a partir do vetor de bytes que estava na coluna BLOB do MySQL
                 BufferedImage image = ImageIO.read(new ByteArrayInputStream(rs.getBinaryStream("foto").readAllBytes()));

                 if (image != null){
                    // Criando um JFrame - janela de aplicação gráfica
                    var frame = new JFrame();
                    // Criando um jLabel com a image
                    var jLabel = new JLabel(new ImageIcon(image));

                    // Adicionando o jLabel na janela
                    frame.add(jLabel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    // Tornando a janela visível 
                    frame.setVisible(true);
                 }else{
                     JOptionPane.showMessageDialog(null, "Cidade não possui foto");
                 }

            }else{
                JOptionPane.showMessageDialog(null, "Cidade não encontrada");
            }
        }catch(SQLException e) {
            System.err.println("Erro ao obter dados do banco de dados: " + e.toString());   
        } catch (IOException e) {
            System.out.println("Erro ao obter vetor de bytes; " +  e.toString());
        }

    }

    public static void main(String[] args) {
     
        var app = new App();

        try {
            // Diálogo com duas opções para o usuário
            String[] opcoes = {"Adicionar cidade", "Ver foto de cidade"};
            int opcaoSelecionada = JOptionPane.showOptionDialog(null, "Escolha a opção desejada", "Cadastro de cidades", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcoes, null);

            switch(opcaoSelecionada){
                // Cadastrar nova cidade
                case 0:
                    var fileChooser = new JFileChooser();
                    var nomeCidade = JOptionPane.showInputDialog(null, "Entre com o nome da cidade");
                    var nomePais = JOptionPane.showInputDialog(null, "Entre com o nome do país");
                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                        app.cadastrarCidade(nomeCidade, nomePais, fileChooser.getSelectedFile());
                    }
                    break;
                // Exibir foto associada a uma cidade
                case 1:
                    String s = JOptionPane.showInputDialog("Informe o id da cidade que deseja ver a foto:");
                    app.exibirFotoDaCidade(Integer.parseInt(s));
                    break;
                default:
                System.out.println("Nenhuma opção selecionada");
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo: " + e.toString());
        }
    }
}
