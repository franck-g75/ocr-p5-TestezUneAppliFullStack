/// <reference types="cypress" />
import { adminEmail, adminTruePassword, userTrueEmail, userTruePassword } from '../support/Constants.ts';

describe('info user spec', () => {
  
  //GIVEN

  it.only('user informations successful', () => {
    
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'user@studio.com',
        firstName: 'Franck',
        lastName: 'GUINDEUIL',
        admin: false
      },
    });


    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
            {
                "id": 3,
                "name": "pilate",
                "date": "1975-04-30T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "superbe cours de pilates...",
                "users": [],
                "createdAt": "2025-12-23T23:03:03",
                "updatedAt": "2026-01-18T21:30:49"
            }
       ]).as('session');




    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/2',
      },
            {
                "id": 2,
                "email": "user@studio.com",
                "lastName": "Franck",
                "firstName": "GUINDEUIL",
                "admin": false,
                "createdAt": "2025-12-25T22:41:25",
                "updatedAt": "2025-12-25T22:41:25"
            }
       ).as('user');

    //WHEN
    
    cy.visit("http://localhost:4200/login");
    
    cy.get('input[formControlName=email]').type(userTrueEmail);
    cy.get('input[formControlName=password]').type(userTruePassword + `{enter}{enter}`);

    //THEN
    
    cy.url().should('include', '/sessions')

    cy.get('[data-cy="meLink"]').click();
    cy.url().should('include', '/me');
    cy.contains("Name: GUINDEUIL FRANCK").should('be.visible');
    cy.contains("Email: user@studio.com").should('be.visible');
    cy.contains("You are admin").should('not.exist');

  });
});


describe('info admin spec', () => {
  
  it.only('admin informations successful', () => {
    
    //GIVEN

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'admin@studio.com',
        firstName: 'peu',
        lastName: 'importe',
        admin: true
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/2',
      },
      
            {
                "id": 1,
                "email": "admin@studio.com",
                "lastName": "admin",
                "firstName": "ADMIN",
                "admin": true,
                "createdAt": "2025-12-22T22:41:25",
                "updatedAt": "2025-12-22T22:41:25"
            }
       ).as('user');

    //WHEN
    
    cy.visit("http://localhost:4200/login");
    
    cy.get('input[formControlName=email]').type(userTrueEmail);
    cy.get('input[formControlName=password]').type(userTruePassword + `{enter}{enter}`);

    //THEN
    cy.url().should('include', '/sessions')

    cy.get('[data-cy="meLink"]').click();
    cy.url().should('include', '/me');
    cy.contains("Name: ADMIN ADMIN").should('be.visible');
    cy.contains("Email: admin@studio.com").should('be.visible');
    cy.contains("You are admin").should('exist');

  });
});