package com.icarodebarros.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void peencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		// OBS.: Numa situação real deveria ter uma chamada à um webService que geraria o boleto,
		// e a data de vencimento do mesmo seria salva aqui
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

}
