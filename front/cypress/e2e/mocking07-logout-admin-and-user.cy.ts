/// <reference types="cypress" />

import { adminEmail, adminTruePassword, userTrueEmail, userTruePassword } from '../support/Constants.ts';

describe('Logout user and admin spec', () => {

//récupéré du sujet
it('Login and logout user successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type(userTrueEmail)
    cy.get('input[formControlName=password]').type(userTruePassword + `{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.get('[data-cy="logoutLink"]').click();

    cy.url().should('not.include.any.keys', 'sessions','login','register','me');




  });





  it('Login and logout admin successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type(adminEmail)
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`)

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="logoutLink"]').click();

    cy.url().should('not.include.any.keys', 'sessions','login','register','me');

  });

});