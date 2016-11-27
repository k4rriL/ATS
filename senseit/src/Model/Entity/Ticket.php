<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Ticket Entity
 *
 * @property int $id
 * @property int $activity_id
 * @property int $parent_id
 * @property string $description
 *
 * @property \App\Model\Entity\Activity $activity
 * @property \App\Model\Entity\Ticket $parent_ticket
 * @property \App\Model\Entity\Ticket[] $child_tickets
 * @property \App\Model\Entity\Worker[] $workers
 */
class Ticket extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * Note that when '*' is set to true, this allows all unspecified fields to
     * be mass assigned. For security purposes, it is advised to set '*' to false
     * (or remove it), and explicitly make individual fields accessible as needed.
     *
     * @var array
     */
    protected $_accessible = [
        '*' => true,
        'id' => false
    ];
}
