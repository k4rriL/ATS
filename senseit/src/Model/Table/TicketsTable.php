<?php
namespace App\Model\Table;

use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * Tickets Model
 *
 * @property \Cake\ORM\Association\BelongsTo $Activities
 * @property \Cake\ORM\Association\BelongsTo $ParentTickets
 * @property \Cake\ORM\Association\HasMany $ChildTickets
 * @property \Cake\ORM\Association\BelongsToMany $Workers
 *
 * @method \App\Model\Entity\Ticket get($primaryKey, $options = [])
 * @method \App\Model\Entity\Ticket newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\Ticket[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\Ticket|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\Ticket patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\Ticket[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\Ticket findOrCreate($search, callable $callback = null)
 */
class TicketsTable extends Table
{

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     * @return void
     */
    public function initialize(array $config)
    {
        parent::initialize($config);

        $this->table('tickets');
        $this->displayField('id');
        $this->primaryKey('id');

        $this->belongsTo('Activities', [
            'foreignKey' => 'activity_id',
            'joinType' => 'INNER'
        ]);
        $this->belongsTo('ParentTickets', [
            'className' => 'Tickets',
            'foreignKey' => 'parent_id'
        ]);
        $this->hasMany('ChildTickets', [
            'className' => 'Tickets',
            'foreignKey' => 'parent_id'
        ]);
        $this->belongsToMany('Workers', [
            'foreignKey' => 'ticket_id',
            'targetForeignKey' => 'worker_id',
            'joinTable' => 'workers_tickets'
        ]);
    }

    /**
     * Default validation rules.
     *
     * @param \Cake\Validation\Validator $validator Validator instance.
     * @return \Cake\Validation\Validator
     */
    public function validationDefault(Validator $validator)
    {
        $validator
            ->integer('id')
            ->allowEmpty('id', 'create');

        $validator
            ->requirePresence('description', 'create')
            ->notEmpty('description');

        return $validator;
    }

    /**
     * Returns a rules checker object that will be used for validating
     * application integrity.
     *
     * @param \Cake\ORM\RulesChecker $rules The rules object to be modified.
     * @return \Cake\ORM\RulesChecker
     */
    public function buildRules(RulesChecker $rules)
    {
        $rules->add($rules->existsIn(['activity_id'], 'Activities'));
        $rules->add($rules->existsIn(['parent_id'], 'ParentTickets'));

        return $rules;
    }
}
