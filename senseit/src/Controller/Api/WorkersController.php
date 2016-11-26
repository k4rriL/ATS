<?php
namespace App\Controller\Api;

use App\Controller\Api\AppController;

/**
 * Workers Controller
 *
 * @property \App\Model\Table\WorkersTable $Workers
 */
class WorkersController extends AppController
{

    /**
     * View method
     *
     * @param string|null $id Worker id.
     * @return \Cake\Network\Response|null
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function view($id = null)
    {
        $worker = $this->Workers->get($id, [
            'contain' => ['Roles', 'Tickets', 'Workers'],
        	'deep' => 3
        ]);

        $this->set('worker', $worker);
        $this->set('_serialize', ['worker']);
    }
}
