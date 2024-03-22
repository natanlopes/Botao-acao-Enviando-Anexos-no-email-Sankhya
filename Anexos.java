package testedeanexos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.bmp.PersistentLocalEntity;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class Anexos implements AcaoRotinaJava {
	String msg;
	BigDecimal nuAnexo;
    @Override
    public void doAction(ContextoAcao ctx) throws Exception {
        Registro linhas[] = ctx.getLinhas();
        if (linhas.length == 1) {
            Registro registro = linhas[0];
            try {
                byte[] anexo = (byte[]) registro.getCampo("ANEXOS");
                String nomeArquivo = "NomeDoArquivoAqui.pdf"; // Você precisará obter esse nome de alguma forma

                // Agora chame enviarEmail passando os dados necessários
                EntityFacade dwfEntityFacade = EntityFacadeFactory.getDWFFacade();
                enviarEmail(dwfEntityFacade, ctx, anexo, nomeArquivo);
                ctx.setMensagemRetorno("Email enviado com sucesso.");
            } catch (Exception e) {
                ctx.setMensagemRetorno("Erro ao processar o anexo: " + e.getMessage());
            }   
        } else {
            ctx.setMensagemRetorno("Nenhuma ou mais de uma linha selecionada.");
        }
    }

  
    private void enviarEmail(EntityFacade dwfEntityFacade, ContextoAcao contexto, byte[] bytesPdf, String nomeArquivo) throws Exception {
		BigDecimal codFila = null;
		JdbcWrapper jdbc = JapeFactory.getEntityFacade().getJdbcWrapper();
		NativeSql nativeSql = new NativeSql(jdbc);
		
		try {

			EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
			DynamicVO dynamicVO1 = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("AnexoMensagem"); // TMDAMG TABELA DE ANEXO DE MENSAGEM SUA PK E NUANEXO 
			dynamicVO1.setProperty("ANEXO", bytesPdf);
			dynamicVO1.setProperty("NOMEARQUIVO", nomeArquivo);
			dynamicVO1.setProperty("TIPO", "application/pdf");
			PersistentLocalEntity createEntity = dwfFacade.createEntity("AnexoMensagem",(EntityVO) dynamicVO1);
			DynamicVO save = (DynamicVO) createEntity.getValueObject();
			// BigDecimal nuAnexo = save.asBigDecimal("NUANEXO");

			nuAnexo = (BigDecimal) save.getProperty("NUANEXO");



		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do anexo " + e.getMessage();
		
		}
		
		String assuntoEmail = "Teste";
		
		char[] assuntoEmailchar = assuntoEmail.toCharArray();
		
		try {
			EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
			DynamicVO dynamicVO1 = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("MSDFilaMensagem"); //  TMDFMG TABELA FILA DE MENSAGEM PK  CODFILA E TEM NUANEXO

			
			dynamicVO1.setProperty("ASSUNTO", "teste");
			dynamicVO1.setProperty("CODMSG", null);
			dynamicVO1.setProperty("DTENTRADA", new Timestamp(System.currentTimeMillis()));
			dynamicVO1.setProperty("STATUS", "Pendente");
//			dynamicVO1.setProperty("CODCON", new BigDecimal(0));
			dynamicVO1.setProperty("TENTENVIO", new BigDecimal(0));
			dynamicVO1.setProperty("MENSAGEM", assuntoEmailchar);

			dynamicVO1.setProperty("TIPOENVIO", "E");
			dynamicVO1.setProperty("MAXTENTENVIO", new BigDecimal(3));
//		    dynamicVO1.setProperty("EMAIL", emailUsuario);
			dynamicVO1.setProperty("EMAIL", "natanael.lopes@argofruta.com");
//			dynamicVO1.setProperty("CODSMTP", new BigDecimal(10));
			dynamicVO1.setProperty("CODUSUREMET", contexto.getUsuarioLogado());

			dynamicVO1.setProperty("NUANEXO", nuAnexo);
			dynamicVO1.setProperty("MIMETYPE", "text/html");
			PersistentLocalEntity createEntity = dwfFacade.createEntity("MSDFilaMensagem", (EntityVO) dynamicVO1);
			DynamicVO save = (DynamicVO) createEntity.getValueObject();
			codFila = save.asBigDecimal("CODFILA");
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do item " + e.getMessage();
		}
		
		try {


			EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
			DynamicVO dynamicVO1 = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("MSDDestFilaMensagem");

			dynamicVO1.setProperty("CODFILA", codFila);
			dynamicVO1.setProperty("EMAIL", "natanael.lopes@argofruta.com");
			dynamicVO1.setProperty("SEQUENCIA", new BigDecimal(1));
			PersistentLocalEntity createEntity = dwfFacade.createEntity("MSDDestFilaMensagem",(EntityVO) dynamicVO1); // TABELA Contatos destinatários da fila de mensagem PK CODFILA
			@SuppressWarnings("unused")
			DynamicVO save = (DynamicVO) createEntity.getValueObject();

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Erro na inclusao do item " + e.getMessage();
		}

	}
//    public static List<String> extractAllNamesUsingRegex(String input) {
//  		List<String> fileNames = new ArrayList<>();
//  		Pattern pattern = Pattern.compile("\"name\":\"([^\"]+)\"");
//  		Matcher matcher = pattern.matcher(input);
//  		while (matcher.find()) {
//  			fileNames.add(matcher.group(1));
//  		}
//  		return fileNames;
//  	}


}

