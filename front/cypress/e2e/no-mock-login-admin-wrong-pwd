/// <reference types="cypress" />
import { adminEmail, adminWrongPassword } from '../support/Constants.ts';
//
//en fait le token est ajouté lors de l'interception de chaque requete (repertoire interceptors) 
// ==> donc pas besoin de gérer le token
//

describe('Login spec', () => {
  it.only('Login successfull', () => {
    
    cy.visit("http://localhost:4200/login");
    cy.get('input[formControlName=email]').type(adminEmail);
    cy.get('input[formControlName=password]').type(adminWrongPassword + `{enter}{enter}`);

    cy.contains("An error occurred").should('be.visible');

    });
    
  });
