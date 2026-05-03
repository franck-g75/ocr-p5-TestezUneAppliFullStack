/// <reference types="cypress" />
import { adminEmail, adminTruePassword } from '../support/Constants.ts';
//
//en fait le token est ajouté lors de l'interception de chaque requete (repertoire interceptors) 
// ==> donc pas besoin de gérer le token
//

describe('Sessions Links verification', () => {

  //pour se connecter a la liste des sessions
  beforeEach(() => {
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
       ]).as('session');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
          {
              "id": 1,
              "lastName": "DELAHAYE",
              "firstName": "Margot",
              "createdAt": "2025-12-21T22:41:06",
              "updatedAt": "2025-12-21T22:41:06"
          },
          {
              "id": 2,
              "lastName": "THIERCELIN",
              "firstName": "Hélène",
              "createdAt": "2025-12-21T22:41:06",
              "updatedAt": "2025-12-21T22:41:06"
          }
      ]).as('teacher');


    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },{
          "id": 1,
          "name": "PILATE",
          "date": "2026-04-11T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Venez nombreux à mon cours de pilate",
          "users": [],
          "createdAt": "2026-03-15T22:49:40",
          "updatedAt": "2026-04-18T11:50:10"
      }).as('session1');

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
