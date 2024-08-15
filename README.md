# Botão de ação enviar Anexos para email - ERP Sankhya
## Descrição
O Anexos é uma classe Java desenvolvida para a plataforma Sankhya, que realiza o processamento de anexos e envia e-mails com esses anexos. O código é integrado como uma ação personalizada e lida com anexos no formato PDF, realizando as seguintes operações:

- Processa Anexos: Extrai e processa anexos recebidos em uma tabela do banco de dados.
- Armazena Anexos: Armazena os anexos em uma tabela específica para mensagens.
- Envia E-mails: Cria e envia e-mails contendo o anexo, configurando a fila de mensagens e destinando os e-mails para os destinatários especificados.
# Funcionalidade
- doAction(ContextoAcao ctx)
- Método principal que é chamado para processar o anexo e enviar o e-mail:

- Obtém Anexo: Recupera o anexo da linha selecionada no contexto da ação.
- Chama enviarEmail: Envia o e-mail passando o anexo e outras informações necessárias.
- Configura Mensagem de Retorno: Define uma mensagem de sucesso ou erro com base no resultado do processamento.
- enviarEmail(EntityFacade dwfEntityFacade, ContextoAcao contexto, byte[] bytesPdf, String nomeArquivo)
- Método auxiliar que realiza a seguinte sequência de operações:

Armazena o Anexo: Salva o anexo na tabela de anexo de mensagem (AnexoMensagem).
Cria Fila de Mensagens: Configura e insere uma nova entrada na tabela de fila de mensagens (MSDFilaMensagem).
Destinatários: Associa o e-mail à fila de mensagens e define os destinatários (MSDDestFilaMensagem).
## Estrutura do Código
# Imports
O código utiliza várias bibliotecas e classes, incluindo:

```java.math.BigDecimal
java.sql.Timestamp
java.util.ArrayList
java.util.List
java.util.regex.Matcher
java.util.regex.Pattern
br.com.sankhya.extensions.actionbutton.AcaoRotinaJava
br.com.sankhya.extensions.actionbutton.ContextoAcao
br.com.sankhya.extensions.actionbutton.Registro
br.com.sankhya.jape.EntityFacade
br.com.sankhya.jape.bmp.PersistentLocalEntity
br.com.sankhya.jape.dao.JdbcWrapper
br.com.sankhya.jape.sql.NativeSql
br.com.sankhya.jape.vo.DynamicVO
br.com.sankhya.jape.vo.EntityVO
br.com.sankhya.jape.wrapper.JapeFactory
br.com.sankhya.modelcore.util.EntityFacadeFactory
 ```
## Erros e Mensagens
- Tratamento de Erros: Em caso de erro durante o processamento ou envio, a mensagem de erro é configurada e exibida.
- Mensagens de Retorno: Mensagens de sucesso ou erro são definidas no contexto da ação.
