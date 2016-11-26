<?php
namespace App\Controller;

use App\Controller\AppController;

/**
 * Workers Controller
 *
 * @property \App\Model\Table\WorkersTable $Workers
 */
class WorkersController extends AppController
{

    /**
     * Index method
     *
     * @return \Cake\Network\Response|null
     */
    public function index()
    {
        $this->paginate = [
            'contain' => ['Roles']
        ];
        $workers = $this->paginate($this->Workers);

        $this->set(compact('workers'));
        $this->set('_serialize', ['workers']);
    }

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
            'contain' => ['Roles', 'Tickets', 'Workers']
        ]);

        $this->set('worker', $worker);
        $this->set('_serialize', ['worker']);
    }

    /**
     * Add method
     *
     * @return \Cake\Network\Response|void Redirects on successful add, renders view otherwise.
     */
    public function add()
    {
        $worker = $this->Workers->newEntity();
        if ($this->request->is('post')) {
            $worker = $this->Workers->patchEntity($worker, $this->request->data);
            if ($this->Workers->save($worker)) {
                $this->Flash->success(__('The worker has been saved.'));

                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error(__('The worker could not be saved. Please, try again.'));
            }
        }
        $roles = $this->Workers->Roles->find('list', ['limit' => 200]);
        $tickets = $this->Workers->Tickets->find('list', ['limit' => 200]);
        $this->set(compact('worker', 'roles', 'tickets'));
        $this->set('_serialize', ['worker']);
    }

    /**
     * Edit method
     *
     * @param string|null $id Worker id.
     * @return \Cake\Network\Response|void Redirects on successful edit, renders view otherwise.
     * @throws \Cake\Network\Exception\NotFoundException When record not found.
     */
    public function edit($id = null)
    {
        $worker = $this->Workers->get($id, [
            'contain' => ['Tickets']
        ]);
        if ($this->request->is(['patch', 'post', 'put'])) {
            $worker = $this->Workers->patchEntity($worker, $this->request->data);
            if ($this->Workers->save($worker)) {
                $this->Flash->success(__('The worker has been saved.'));

                return $this->redirect(['action' => 'index']);
            } else {
                $this->Flash->error(__('The worker could not be saved. Please, try again.'));
            }
        }
        $roles = $this->Workers->Roles->find('list', ['limit' => 200]);
        $tickets = $this->Workers->Tickets->find('list', ['limit' => 200]);
        $this->set(compact('worker', 'roles', 'tickets'));
        $this->set('_serialize', ['worker']);
    }

    /**
     * Delete method
     *
     * @param string|null $id Worker id.
     * @return \Cake\Network\Response|null Redirects to index.
     * @throws \Cake\Datasource\Exception\RecordNotFoundException When record not found.
     */
    public function delete($id = null)
    {
        $this->request->allowMethod(['post', 'delete']);
        $worker = $this->Workers->get($id);
        if ($this->Workers->delete($worker)) {
            $this->Flash->success(__('The worker has been deleted.'));
        } else {
            $this->Flash->error(__('The worker could not be deleted. Please, try again.'));
        }

        return $this->redirect(['action' => 'index']);
    }
}
