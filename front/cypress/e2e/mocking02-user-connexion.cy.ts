/// <reference types="cypress" />
import { adminEmail, adminTruePassword, userTrueEmail, userTruePassword } from '../support/Constants.ts';
import { userWrongEmail, userWrongPassword, userTooShortPassword, userTooLongPassword,userTooShortFirstName,userTooLongFirstName,userTooShortLastName,userTooLongLastName } from '../support/Constants.ts';

describe('Login spec', () => {
  
  it('Login unsuccessful empty password', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type(userTrueEmail)
    //no password
    cy.get('input[formControlName=email]').focus()

    cy.get('input[formControlName=password]').should('have.class','ng-invalid');
    cy.url().should('include', '/login')
    cy.get('[data-cy="submitButton"]').should('be.disabled');
  });

  it('Login unsuccessfull unauthorized, bad credentials', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        statusCode: 401,
        body: {
            "path": "/api/auth/login",
            "error": "Unauthorized",
            "message": "Bad credentials"
        },
    });

    cy.get('input[formControlName=email]').type(userTrueEmail)
    cy.get('input[formControlName=password]').type(userWrongPassword + `{enter}{enter}`)//peu importe la saisie le mock cache tout

    cy.url().should('include', '/login')
    cy.contains("An error occurred").should('be.visible');
  })


//récupéré du sujet
it('Login successfull', () => {
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
  });


});