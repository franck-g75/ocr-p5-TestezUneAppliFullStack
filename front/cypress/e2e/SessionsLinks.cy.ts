/// <reference types="cypress" />

var token: string;

//before All grab the token
before(() => {

  cy.request("POST", 'http://localhost:8080/api/auth/login', {
              "email": "yoga@studio.com",
              "password": "test!1234"
        }).then((response) => {
              token = response.body.token;
              cy.log(token);
    });
    
});

describe('Sessions Links verifiication', () => {

  //pour se connecter avec le token
  beforeEach(() => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          token: token,
          username: 'yoga@studio.com',
          firstName: 'Admin',
          lastName: 'Admin',
          admin: true
        },
    })

    cy.intercept('http://localhost:8080/api/session', { middleware: true }, (req) => {
        req.headers['authorization'] = `Token ${token}`
      }).as('session')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

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
