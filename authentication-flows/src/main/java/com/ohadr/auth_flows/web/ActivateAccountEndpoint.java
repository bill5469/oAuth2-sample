package com.ohadr.auth_flows.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;

import com.ohadr.auth_flows.interfaces.AuthenticationAccountRepository;

@Controller
@RequestMapping(value = "/aa")
public class ActivateAccountEndpoint extends FlowsEndpointsCommon 
{
	@Autowired
	private AuthenticationAccountRepository oAuthRepository;

	
	@RequestMapping
    public void activateAccount(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
		String redirectUri;

		EmailExtractedData extractedData = extractEmailData(request);
		
		
		
		if (!extractedData.expired)
		{
//TODO			PlatformTransactionManager transactionManager = (PlatformTransactionManager) ContextLoader.getCurrentWebApplicationContext()
//			                                                                                          .getBean("oAuthTransactionManager");
//
//			TransactionStatus oAuthTransaction = transactionManager.getTransaction(new DefaultTransactionAttribute());

			// enable the account
			oAuthRepository.setEnabled(extractedData.userEmail);
			// reset the #attempts, since there is a flow of exceeding attempts number, so when clicking the link
			// (in the email), we get here and enable the account and reset the attempts number
			oAuthRepository.resetAttemptsCounter(extractedData.userEmail);

//TODO			transactionManager.commit(oAuthTransaction);

			request.getSession().invalidate();
			request.getSession(true);
			SecurityContextHolder.getContext().setAuthentication(null);

//			if (redirectUri == null || redirectUri.isEmpty())
//			{
//				redirectUri = extractedData.redirectUri;
//
//				// fallback: (we might get here if login failed and account is locked and we have no redirect-uri:
//				if (redirectUri == null || redirectUri.isEmpty())
//				{
					redirectUri = request.getContextPath() + "/login/AccountActivated.htm";
//				}
//			}

			response.sendRedirect(redirectUri);
		}
	}
}