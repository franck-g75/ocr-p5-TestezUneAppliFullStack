/// <reference types="cypress" />
import { adminEmail, adminTruePassword } from '../support/Constants.ts';

describe('Login spec', () => {
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

    cy.get('input[formControlName=email]').type(adminEmail)
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});