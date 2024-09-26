package com.pfe.backend.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        // Initialiser la clé secrète Stripe après injection
        Stripe.apiKey = stripeSecretKey;
    }

    public String createPaymentIntent(Long amount, String currency) throws Exception {
        // Créer les paramètres pour PaymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount) // Montant en centimes
                .setCurrency(currency) // Devise
                .build();

        // Créer le PaymentIntent
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Retourner le client_secret pour la confirmation du paiement
        return paymentIntent.getClientSecret();
    }
}
