/**
 * @type {Cypress.PluginConfig}
 */
import * as registerCodeCoverageTasks from '@cypress/code-coverage/task';

  export default (on: Cypress.PluginEvents, config: Cypress.PluginConfigOptions) => {
    return registerCodeCoverageTasks(on, config);
  };
