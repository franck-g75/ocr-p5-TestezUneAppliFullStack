/// <reference types="cypress" />
import { adminEmail, adminTruePassword, userTrueEmail, userTruePassword } from '../support/Constants.ts';




describe('FormComponent.form not contain XSS vulnerability', () => {


  it('should not show XSS alert box', () => {

  cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('login');



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
            "lastName": "DELA",
            "firstName": "Margot",
            "createdAt": "2025-12-21T22:41:06",
            "updatedAt": "2025-12-21T22:41:06"
        },
        {
            "id": 2,
            "lastName": "THIERCE",
            "firstName": "Hélène",
            "createdAt": "2025-12-21T22:41:06",
            "updatedAt": "2025-12-21T22:41:06"
        }
    ]).as('teacher');



  cy.visit("http://localhost:4200/login");
    
  cy.get('input[formControlName=email]').type(adminEmail);
  cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`);

  cy.get('[data-cy="btnCreate"]').click();

  cy.get('[data-cy="name"]').type('<script>alert("XSS Name");</script>');
  cy.get('[data-cy="date"]').type('2025-12-27')//date selection
  cy.get('[data-cy="teacher_id"]').click()//teacher selection
  cy.get('[data-cy="teacher_option"]').contains('Margot DELA').click();
  cy.get('[data-cy="description"]').type('<script>alert("XSS description");</script>');
  cy.get('[data-cy="btnSave"]').click()
 
  cy.on('window:alert', () => { //never saw
    assert.Throw(() => "Une fenêtre d\'alerte s\'est affichée dans le formulaire creation / mise à jour de session ! ");
  });

  });

});

























describe('session creation admin spec', () => {
  
  it('Session creation successfull', () => {
    
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('login');

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
            "lastName": "DELA",
            "firstName": "Margot",
            "createdAt": "2025-12-21T22:41:06",
            "updatedAt": "2025-12-21T22:41:06"
        },
        {
            "id": 2,
            "lastName": "THIERCE",
            "firstName": "Hélène",
            "createdAt": "2025-12-21T22:41:06",
            "updatedAt": "2025-12-21T22:41:06"
        }
    ]).as('teacher');


    //etrange pas obligé d'ajouter les informations de la session
    cy.intercept(
        {
        method: 'POST',
        url: '/api/session'
        },
        {
            "id": 2,
            "name": "reflexo",
            "date": "1975-04-30T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "coupez vous les pieds",
            "users": [],
            "createdAt": "2026-03-15T20:07:21.5697163",
            "updatedAt": "2026-03-15T20:07:21.6039447"
        }
      ).as('sessioncreate');

    cy.visit("http://localhost:4200/login");
    
    cy.get('input[formControlName=email]').type(adminEmail);
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="btnCreate"]').should('be.enabled');
    cy.get('[data-cy="btnCreate"]').click();

    cy.url().should('include', '/sessions/create')

    cy.get('[data-cy="name"]').type("reflexologie plantaire");
    cy.get('[data-cy="date"]').type("2025-12-26");
    cy.get('[data-cy="teacher_id"]').first().click().get('mat-option').contains('Margot DELA').click();
    cy.get('[data-cy="description"]').type("coupez vous les pieds");
    cy.get('[data-cy="btnSave"]').click();

    //2 url appelées POST api/sessions et GET api/sessions

    cy.url().should('include', '/sessions'); //tout est bon
    
    });
});










describe('session modification admin spec', () => {
  
  it('Session modification successfull', () => {
    

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
        url: '/api/session/3',
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
       ]).as('session1');



    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
        [
        {
            "id": 1,
            "lastName": "DELA",
            "firstName": "Margot",
            "createdAt": "2025-12-21T22:41:06",
            "updatedAt": "2025-12-21T22:41:06"
        },
        {
            "id": 2,
            "lastName": "THIERCE",
            "firstName": "Hélène",
            "createdAt": "2025-12-21T22:41:06",
            "updatedAt": "2025-12-21T22:41:06"
        }
    ]).as('teacher');


    //etrange pas obligé d'ajouter les informations de la session
    cy.intercept(
        {
        method: 'PUT',
        url: '/api/session/3'
        },
        {
            "id": 3,
            "name": "reflexo",
            "date": "1975-04-30T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "coupez vous les pieds",
            "users": [],
            "createdAt": "2026-03-15T20:07:21.5697163",
            "updatedAt": "2026-03-15T20:07:21.6039447"
        }
      ).as('sessioncreate');






    cy.visit("http://localhost:4200/login");
    
    cy.get('input[formControlName=email]').type(adminEmail);
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="btnEdit"]').should('be.enabled');
    cy.get('[data-cy="btnEdit"]').click();

    cy.url().should('include', '/sessions/update/');
    cy.contains("Update session").should('be.visible');
    

    cy.url().should('include', '/sessions'); 
    
    });
});




















describe('session delete admin spec', () => {
  
  it('Session delete successfull', () => {
    
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('login');


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
       ]).as('session1');


    cy.intercept(
        {
        method: 'DELETE',
        url: '/api/session/1'
        },
        {statusCode: 200}
      ).as('sessiondeleted');


    cy.visit("http://localhost:4200/login");
    
    cy.get('input[formControlName=email]').type(adminEmail);
    cy.get('input[formControlName=password]').type(adminTruePassword + `{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('[data-cy="btnDetail"]').should('be.enabled');

    cy.get('[data-cy="btnDetail"]').click();

    cy.url().should('include', '/sessions/detail/')

    cy.get('[data-cy="btnDelete"]').should('be.enabled');

    cy.get('[data-cy="btnDelete"]').click();

    //2 url appelées POST api/sessions et GET api/sessions

    cy.url().should('include', '/sessions'); //tout est bon
    
    });
});