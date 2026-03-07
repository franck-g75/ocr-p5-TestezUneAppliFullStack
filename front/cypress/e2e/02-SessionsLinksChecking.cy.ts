/// <reference types="cypress" />
import { adminEmail, adminTruePassword } from '../support/Constants.ts';
//
//en fait le token est ajouté lors de l'interception de chaque requete (repertoire interceptors) 
// ==> donc pas besoin de gérer le token
//


//before All : connecting as admin
before(() => {

  cy.request( "POST", 'http://localhost:8080/api/auth/login',   { "email": adminEmail, "password": adminTruePassword });
  
});

describe('Sessions Links verification', () => {

  //pour se connecter a la liste des sessions
  beforeEach(() => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type(adminEmail)
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`)
    //sessions
  });

  it('create link and return link successfull', () => {
    cy.get('[data-cy="btnCreate"]').click()
    cy.url().should('include', '/create')
    cy.get('[data-cy="btnReturn"]').click()
    cy.url().should('include', '/sessions')
  });

  it('details link and return successfull', () => {
    cy.get('[data-cy="btnDetail"]').click()
    cy.url().should('include', '/detail')
    cy.get('[data-cy="btnReturn"]').click()
    cy.url().should('include', '/sessions')
  });

  it('edit link and return successfull', () => {
    cy.get('[data-cy="btnEdit"]').click()
    cy.url().should('include', '/update/')
    cy.get('[data-cy="btnReturn"]').click()
    cy.url().should('include', '/sessions')
  });

});
