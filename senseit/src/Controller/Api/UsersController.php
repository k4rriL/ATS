<?php

namespace App\Controller\Api;

use App\Controller\Api\AppController;

use Cake\Event\Event;
use Cake\Network\Exception\UnauthorizedException;
use Cake\Utility\Security;
use Firebase\JWT\JWT;

/**
 * Users Controller
 *
 * @property \App\Model\Table\UsersTable $Users
 */
class UsersController extends AppController
{
	
    public function initialize()
    {
        parent::initialize();
        $this->Auth->allow(['token']);
    }

    /**
     * Index method
     *
     * @return \Cake\Network\Response|null
     */
    public function index()
    {
        $users = $this->paginate($this->Users);

        $this->set(compact('users'));
        $this->set('_serialize', ['users']);
    }
	
	public function token()
	{
	    $user = $this->Auth->identify();
	    if (!$user) {
	        throw new UnauthorizedException('Invalid username or password');
	    }
	    $this->set([
	        'success' => true,
	        'data' => [
	            'token' => JWT::encode([
	                'sub' => $user['id'],
	                'exp' =>  time() + 604800
	            ],
	            Security::salt()),
	         'id' => $user['id']
	        ],
	        '_serialize' => ['success', 'data']
	    ]);
	}
   
}
