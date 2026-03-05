/// <reference types="cypress" />
//import { faker } from '@faker-js/faker';

const fakeEmail = "yoga@studio.com"; //faker.internet.email();
const fakePassword = "test!1234";    //faker.internet.password({ length: 20 });
var token: string;

before(() => {
      cy.request("POST", 'http://localhost:8080/api/auth/login', {
            "email": fakeEmail,
            "password": fakePassword
      }).then((response) => {
            token = response.body.token;
      });
      cy.log(token);
});

describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login');

    //localStorage.setItem('token', token);
    //window.localStorage.setItem('token', token);

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        token : token,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get(token).should('not.be.empty');
    cy.log(token);
 
    cy.intercept('http://localhost:8080/api/session', { middleware: true }, (req) => {
      req.headers['authorization'] = `Token ${token}`
    }).as('session')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    
  });

});

