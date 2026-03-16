/// <reference types="cypress" />
import { adminEmail, adminTruePassword, userTrueEmail, userTruePassword } from '../support/Constants.ts';

describe('session detail user display spec', () => {
  
  it.only('Session detail user display successfull (delete does not exist)', () => {
    
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      [
            {
                "id": 1,
                "name": "pilate",
                "date": "1975-04-30T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "superbe cours de pilates...",
                "users": [],
                "createdAt": "2025-12-23T23:03:03",
                "updatedAt": "2026-01-18T21:30:49"
            }
       ]).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
            {
                "id": 1,
                "name": "pilate",
                "date": "1975-04-30T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "superbe cours de pilates...",
                "users": [],
                "createdAt": "2025-12-23T23:03:03",
                "updatedAt": "2026-01-18T21:30:49"
            }
       ]).as('session')
    
    cy.visit("http://localhost:4200/login");
    
    cy.get('input[formControlName=email]').type(userTrueEmail);
    cy.get('input[formControlName=password]').type(userTruePassword + `{enter}{enter}`);

    cy.url().should('include', '/sessions')

    cy.get('[data-cy="btnDetail"]').click()

    cy.url().should('include', '/sessions/detail/')
    cy.get('[data-cy="btnDelete"]').should('not.exist');

  })
});







describe('session detail admin display spec', () => {
 
  it.only('Session detail admin display successfull (delete is enabled)', () => {
    
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
      [
            {
                "id": 1,
                "name": "pilate",
                "date": "1975-04-30T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "superbe cours de pilates...",
                "users": [],
                "createdAt": "2025-12-23T23:03:03",
                "updatedAt": "2026-01-18T21:30:49"
            }
       ]).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      [
            {
                "id": 1,
                "name": "pilate",
                "date": "1975-04-30T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "superbe cours de pilates...",
                "users": [],
                "createdAt": "2025-12-23T23:03:03",
                "updatedAt": "2026-01-18T21:30:49"
            }
       ]).as('session')
       
    cy.visit("http://localhost:4200/login");

    cy.get('input[formControlName=email]').type(adminEmail);
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`);
    
    cy.url().should('include', '/sessions');
    
    cy.get('[data-cy="btnDetail"]').click();

    cy.url().should('include', '/sessions/detail/');
    cy.get('[data-cy="btnDelete"]').should('be.enabled');
    
  });
});