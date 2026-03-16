/// <reference types="cypress" />
import { userTrueFirstName, userTrueLastName, userTrueEmail, userTruePassword } from '../support/Constants.ts';
import { userWrongEmail,userTooShortPassword, userTooLongPassword,userTooShortFirstName,userTooLongFirstName,userTooShortLastName,userTooLongLastName } from '../support/Constants.ts';
describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
            "message": "User registered successfully!"
        },
    });

    cy.get('input[formControlName=firstName]').type(userTrueFirstName);
    cy.get('input[formControlName=lastName]').type(userTrueLastName);
    cy.get('input[formControlName=email]').type(userTrueEmail);
    cy.get('input[formControlName=password]').type(userTruePassword + `{enter}{enter}`);
    
    cy.url().should('include', '/login');
  });


  it('Register UNSUCCESSFULL (too short)', () => {
    cy.visit('/register');

    cy.get('input[formControlName=firstName]').type(userTooShortFirstName);
    cy.get('input[formControlName=lastName]').type(userTooShortLastName);
    cy.get('input[formControlName=email]').type(userWrongEmail);
    cy.get('input[formControlName=password]').type(userTooShortPassword);
    cy.get('input[formControlName=email]').focus();

    cy.get('[data-cy="submitButton"]').should('be.disabled');

    cy.get('input[formControlName=firstName]').should('have.class','ng-invalid');
    cy.get('input[formControlName=lastName]').should('have.class','ng-invalid');
    cy.get('input[formControlName=email]').should('have.class','ng-invalid');
    cy.get('input[formControlName=password]').should('have.class','ng-invalid');

  });

  it('Register UNSUCCESSFULL (too long)', () => {
    cy.visit('/register');

    cy.get('input[formControlName=firstName]').type(userTooLongFirstName);
    cy.get('input[formControlName=lastName]').type(userTooLongLastName);
    cy.get('input[formControlName=email]').type(userWrongEmail);
    cy.get('input[formControlName=password]').type(userTooLongPassword);
    cy.get('input[formControlName=email]').focus();

    cy.get('[data-cy="submitButton"]').should('be.disabled');

    cy.get('input[formControlName=firstName]').should('have.class','ng-invalid');
    cy.get('input[formControlName=lastName]').should('have.class','ng-invalid');
    cy.get('input[formControlName=email]').should('have.class','ng-invalid');
    cy.get('input[formControlName=password]').should('have.class','ng-invalid');

  });

});