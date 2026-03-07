/// <reference types="cypress" />
import { adminEmail, adminTruePassword } from '../support/Constants.ts';
//
//en fait le token est ajouté lors de l'interception de chaque requete (repertoire interceptors) 
// ==> donc pas besoin de gérer le token
//

before(() => {
  
    cy.visit("http://localhost:4200/login");
    cy.get('input[formControlName=email]').type(adminEmail);
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`);

});

describe('Login spec', () => {
  it.only('Login successfull', () => {

      //cy.visit("http://localhost:4200/sessions");
      cy.contains("Rentals available").should('be.visible');

    });
    
  });
